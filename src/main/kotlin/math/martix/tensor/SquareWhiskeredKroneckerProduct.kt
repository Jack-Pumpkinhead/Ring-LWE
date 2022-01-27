package math.martix.tensor

import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.Ring
import math.coding.LadderIndex
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.mutable.AbstractMutableMatrix
import math.martix.zeroMutableMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/27 16:30
 *
 * represent a square matrix of the form I_l ⊗ M ⊗ I_r for M square
 */
class SquareWhiskeredKroneckerProduct<A>(ring: Ring<A>, val l: UInt, val mA: AbstractSquareMatrix<A>, val r: UInt) : AbstractSquareMatrix<A>(ring, l * mA.rows * r) {

    val index = LadderIndex(listOf(l, mA.rows, r), super.rows)

    init {
        lazyAssert2 {
            val bigL = l.toBigInteger()
            val bigR = r.toBigInteger()
            val rows = bigL * mA.rows.toBigInteger() * bigR
            assert(rows <= UInt.MAX_VALUE)
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        val row1 = index.decode(row)
        val column1 = index.decode(column)
        return if (row1[0] == column1[0] && row1[2] == column1[2]) {
            mA.elementAtUnsafe(row1[1], column1[1])
        } else ring.zero
    }

    override fun determinant(): A {
        TODO("tensor product should have a way of calculating determinants")
    }

    override fun hasInverse(): Boolean {
        return mA.hasInverse()
    }

    override fun inverse(): AbstractSquareMatrix<A> {
        return SquareWhiskeredKroneckerProduct(ring, l, mA.inverse(), r)
    }

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        val dest = ring.zeroMutableMatrix(this.rows, matrix.columns)
        multiplyToImpl(matrix, dest)
        return dest.downCast()
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        val dest = ring.zeroMutableMatrix(this.rows, matrix.columns)
        multiplyToRowParallelImpl(matrix, dest)
        return dest.downCast()
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        for (il in 0u until l) {
            for (ir in 0u until r) {
                val submatrix = matrix.sparseSubmatrixView(il * mA.columns * r + ir, r, mA.columns)
                val destSubmatrix = dest.mutableSparseSubmatrixView(il * mA.rows * r + ir, r, mA.rows)
                mA.multiplyTo(submatrix, destSubmatrix)
            }
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) = coroutineScope {
        for (il in 0u until l) {
            launch {    //reduce O(n^2) coroutine to O(n) coroutine improves performance, but still can't outperform one thread algorithm.
                for (ir in 0u until r) {
                    val submatrix = matrix.sparseSubmatrixView(il * mA.columns * r + ir, r, mA.columns)
                    val destSubmatrix = dest.mutableSparseSubmatrixView(il * mA.rows * r + ir, r, mA.rows)
                    mA.multiplyTo(submatrix, destSubmatrix)
                }
            }
        }
    }
}