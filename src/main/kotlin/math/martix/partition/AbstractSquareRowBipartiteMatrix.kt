package math.martix.partition

import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/29 22:55
 */
interface AbstractSquareRowBipartiteMatrix<A> : AbstractRowBipartiteMatrix<A>, AbstractSquareMatrix<A> {

    override val ring: Ring<A> get() = upper.ring
    override val rows: UInt get() = upper.columns
    override val columns: UInt get() = upper.columns
    override val size: UInt get() = upper.columns

    override fun elementAtUnsafe(row: UInt, column: UInt): A =
        if (row < upper.rows) {
            upper.elementAtUnsafe(row, column)
        } else {
            lower.elementAtUnsafe(row - upper.rows, column)
        }

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = RowBipartiteMatrix(upper.timesImpl(matrix), lower.timesImpl(matrix))

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = RowBipartiteMatrix(upper.timesRowParallelImpl(matrix), lower.timesRowParallelImpl(matrix))

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        upper.multiplyToImpl(matrix, dest.mutableSubmatrixViewAt(0u, 0u, upper.rows, matrix.columns))
        lower.multiplyToImpl(matrix, dest.mutableSubmatrixViewAt(upper.rows, 0u, lower.rows, matrix.columns))
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        upper.multiplyToRowParallelImpl(matrix, dest.mutableSubmatrixViewAt(0u, 0u, upper.rows, matrix.columns))
        lower.multiplyToRowParallelImpl(matrix, dest.mutableSubmatrixViewAt(upper.rows, 0u, lower.rows, matrix.columns))
    }
}