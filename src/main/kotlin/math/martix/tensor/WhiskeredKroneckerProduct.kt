package math.martix.tensor

import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.CoroutineScope
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
 * represent a matrix of the form I_l ⊗ M ⊗ I_r
 */
class WhiskeredKroneckerProduct<A>(override val ring: Ring<A>, override val rows: UInt, override val columns: UInt, val l: UInt, val mA: AbstractMatrix<A>, val r: UInt) : AbstractMatrix<A> {

    val rowIndex = LadderIndex(listOf(l, mA.rows, r), rows)

    val columnIndex = LadderIndex(listOf(l, mA.columns, r), columns)

    init {
        lazyAssert2 {
            require(rows.toBigInteger() == l.toBigInteger() * mA.rows.toBigInteger() * r.toBigInteger())
            require(columns.toBigInteger() == l.toBigInteger() * mA.columns.toBigInteger() * r.toBigInteger())
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
        return dest
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        val dest = ring.zeroMutableMatrix(this.rows, matrix.columns)
        multiplyToRowParallelImpl(matrix, dest)
        return dest
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        var ic1 = 0u
        var ir1 = 0u
        val cStep = mA.columns * r
        val rStep = mA.rows * r
        while (ic1 < columns) {
            var ic2 = ic1
            var ir2 = ir1
            while (ic2 < ic1 + r) {
                val submatrix = matrix.rowSparseSubmatrixView(ic2, r, mA.columns)
                val destSubmatrix = dest.mutableRowSparseSubmatrixView(ir2, r, mA.rows)
                mA.multiplyTo(submatrix, destSubmatrix)
                ic2++
                ir2++
            }
            ic1 += cStep
            ir1 += rStep
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) = coroutineScope {
        var ic1 = 0u
        var ir1 = 0u
        val cStep = mA.columns * r
        val rStep = mA.rows * r
        while (ic1 < columns) {
            launchSubmatrixEdition(this, ic1, ir1, matrix, dest)
            ic1 += cStep
            ir1 += rStep
        }
    }

    //launch naively result in wrong behaviour, should keep ic1/ir1 not changed.
    private fun launchSubmatrixEdition(scope: CoroutineScope, ic1: UInt, ir1: UInt, matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        scope.launch {
            var ic2 = ic1
            var ir2 = ir1
            while (ic2 < ic1 + r) {
                val submatrix = matrix.rowSparseSubmatrixView(ic2, r, mA.columns)
                val destSubmatrix = dest.mutableRowSparseSubmatrixView(ir2, r, mA.rows)
                mA.multiplyTo(submatrix, destSubmatrix)
                ic2++
                ir2++
            }
        }
    }
}