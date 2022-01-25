package math.martix.tensor

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.Ring
import math.coding.BigLadderIndex
import math.martix.AbstractMatrix
import math.martix.mutable.AbstractMutableMatrix
import math.martix.zeroMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/9 17:39
 *
 * represent a matrix in the form I_l ⊗ M ⊗ I_r
 */
class WhiskeredKroneckerProduct<A>(ring: Ring<A>, l: BigInteger, val mA: AbstractMatrix<A>, r: BigInteger) : AbstractMatrix<A>(ring, (l * mA.rows.toBigInteger() * r).uintValue(true), (l * mA.columns.toBigInteger() * r).uintValue(true)) {

    val ul = l.uintValue(true)
    val ur = r.uintValue(true)

    val rowIndex = BigLadderIndex(listOf(l, mA.rows.toBigInteger(), r), super.rows.toBigInteger())  //TODO make index small
    val columnIndex = BigLadderIndex(listOf(l, mA.columns.toBigInteger(), r), super.columns.toBigInteger())

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
        for (il in 0u until ul) {
            for (ir in 0u until ur) {
                val submatrix = matrix.sparseSubmatrixView(il * mA.columns * ur + ir, ur, mA.columns)
                val destSubmatrix = dest.mutableSparseSubmatrixView(il * mA.rows * ur + ir, ur, mA.rows)
                mA.multiplyTo(submatrix, destSubmatrix)
            }
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) = coroutineScope {
        for (il in 0u until ul) {
            launch {    //reduce O(n^2) coroutine to O(n) coroutine improves performance, but still cannot outperform one thread algorithm.
                for (ir in 0u until ur) {
                    val submatrix = matrix.sparseSubmatrixView(il * mA.columns * ur + ir, ur, mA.columns)
                    val destSubmatrix = dest.mutableSparseSubmatrixView(il * mA.rows * ur + ir, ur, mA.rows)
                    mA.multiplyTo(submatrix, destSubmatrix)
                }
            }
        }
    }
}