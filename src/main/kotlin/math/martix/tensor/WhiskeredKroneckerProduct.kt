package math.martix.tensor

import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.Ring
import math.coding.LadderIndex
import math.martix.AbstractMatrix
import math.martix.mutable.AbstractMutableMatrix
import math.martix.zeroMutableMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/9 17:39
 *
 * represent a matrix in the form I_l ⊗ M ⊗ I_r
 */
class WhiskeredKroneckerProduct<A>(ring: Ring<A>, val l: UInt, val mA: AbstractMatrix<A>, val r: UInt) : AbstractMatrix<A>(ring, l * mA.rows * r, l * mA.columns * r) {

    val rowIndex = LadderIndex(listOf(l, mA.rows, r), super.rows)
    val columnIndex = LadderIndex(listOf(l, mA.columns, r), super.columns)

    init {
        lazyAssert2 {
            val bigL = l.toBigInteger()
            val bigR = r.toBigInteger()
            val rows = bigL * mA.rows.toBigInteger() * bigR
            val columns = bigL * mA.columns.toBigInteger() * bigR
            assert(rows <= UInt.MAX_VALUE)
            assert(columns <= UInt.MAX_VALUE)
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        val row1 = rowIndex.decode(row)
        val column1 = columnIndex.decode(column)
        return if (row1[0] == column1[0] && row1[2] == column1[2]) {
            mA.elementAtUnsafe(row1[1], column1[1])
        } else ring.zero
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