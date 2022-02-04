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
import util.stdlib.stepForward

/**
 * Created by CowardlyLion at 2022/1/27 16:30
 *
 * represent a square matrix of the form I_l ⊗ M ⊗ I_r for M square
 */
class SquareWhiskeredKroneckerProduct<A>(override val ring: Ring<A>, val l: UInt, val mA: AbstractSquareMatrix<A>, val r: UInt) : AbstractSquareMatrix<A> {

    override val size: UInt = l * mA.size * r

    val index = LadderIndex(listOf(l, mA.size, r), size)

    init {
        lazyAssert2 {
            val bigL = l.toBigInteger()
            val bigR = r.toBigInteger()
            val rows = bigL * mA.size.toBigInteger() * bigR
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
        for (i1 in 0u until size stepForward mA.size * r) {
            for (i2 in i1 until i1 + r) {
                mA.multiplyTo(matrix.rowSparseSubmatrixView(i2, r, mA.size), dest.mutableRowSparseSubmatrixView(i2, r, mA.size))
            }
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) = coroutineScope {
        for (i1 in 0u until size stepForward mA.size * r) {
            launch {    //safe here
                for (i2 in i1 until i1 + r) {
                    mA.multiplyTo(matrix.rowSparseSubmatrixView(i2, r, mA.size), dest.mutableRowSparseSubmatrixView(i2, r, mA.size))
                }
            }
        }
    }

}