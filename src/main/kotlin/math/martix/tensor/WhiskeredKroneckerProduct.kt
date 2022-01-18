package math.martix.tensor

import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.Ring
import math.coding.LadderIndex
import math.martix.AbstractMatrix
import math.martix.mutable.AbstractMutableMatrix
import math.martix.zeroMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/9 17:39
 *
 * represent a matrix in the form I_l ⊗ M ⊗ I_r
 */
class WhiskeredKroneckerProduct<A>(ring: Ring<A>, val l: UInt, val mA: AbstractMatrix<A>, val r: UInt) : AbstractMatrix<A>(ring, l * mA.rows * r, l * mA.columns * r) {


    val rowIndex = LadderIndex(listOf(l.toBigInteger(), mA.rows.toBigInteger(), r.toBigInteger()))
    val columnIndex = LadderIndex(listOf(l.toBigInteger(), mA.columns.toBigInteger(), r.toBigInteger()))

    init {
        require(rowIndex.indexBound <= UInt.MAX_VALUE)
        require(columnIndex.indexBound <= UInt.MAX_VALUE)
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        val rowIndices = rowIndex.decode(row.toBigInteger())
        val columnIndices = columnIndex.decode(column.toBigInteger())
        return if (rowIndices[0] == columnIndices[0] && rowIndices[2] == columnIndices[2]) {
            mA.elementAtUnsafe(rowIndices[1].uintValue(), columnIndices[1].uintValue())
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
            launch {    //reduce O(n^2) coroutine to O(n) coroutine improves performance, but still cannot outperform one thread algorithm.
                for (ir in 0u until r) {
                    val submatrix = matrix.sparseSubmatrixView(il * mA.columns * r + ir, r, mA.columns)
                    val destSubmatrix = dest.mutableSparseSubmatrixView(il * mA.rows * r + ir, r, mA.rows)
                    mA.multiplyTo(submatrix, destSubmatrix)
                }
            }
        }
    }
}