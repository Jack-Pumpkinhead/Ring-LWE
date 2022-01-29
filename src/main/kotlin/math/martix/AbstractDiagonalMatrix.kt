package math.martix

import math.martix.concrete.Constant
import math.martix.mutable.AbstractMutableMatrix
import math.operation.product
import math.vector.VectorLike

/**
 * Created by CowardlyLion at 2022/1/17 11:54
 */
interface AbstractDiagonalMatrix<A> : AbstractSquareMatrix<A>, VectorLike<A> {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = if (row != column) ring.zero else vectorElementAtUnsafe(row)

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is Constant<A>             -> Constant(ring, ring.multiply(this.vectorElementAtUnsafe(0u), matrix.value))
        is AbstractRowVector<A>    -> ring.rowVector(matrix.columns) { i -> ring.multiply(this.vectorElementAtUnsafe(0u), matrix.vectorElementAtUnsafe(i)) }
        is AbstractColumnVector<A> -> ring.columnVector(this.rows) { i -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.vectorElementAtUnsafe(i)) }
        is IdentityMatrix<A>       -> this
        is ZeroMatrix<A>           -> ZeroMatrix(ring, this.rows, matrix.columns)
        else                       -> ring.matrix(matrix.rows, matrix.columns) { i, j -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.elementAtUnsafe(i, j)) }
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is Constant<A>             -> Constant(ring, ring.multiply(this.vectorElementAtUnsafe(0u), matrix.value))
        is AbstractRowVector<A>    -> ring.rowVector(matrix.columns) { i -> ring.multiply(this.vectorElementAtUnsafe(0u), matrix.vectorElementAtUnsafe(i)) }
        is AbstractColumnVector<A> -> ring.columnVectorParallel(this.rows) { i -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.vectorElementAtUnsafe(i)) }
        is IdentityMatrix<A>       -> this
        is ZeroMatrix<A>           -> ZeroMatrix(ring, this.rows, matrix.columns)
        else                       -> ring.matrixRowParallel(matrix.rows, matrix.columns) { i, j -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.elementAtUnsafe(i, j)) }
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>             -> dest.setElementAtUnsafe(0u, 0u, ring.multiply(this.vectorElementAtUnsafe(0u), matrix.value))
            is AbstractRowVector<A>    -> dest.set { _, j -> ring.multiply(this.vectorElementAtUnsafe(0u), matrix.vectorElementAtUnsafe(j)) }
            is AbstractColumnVector<A> -> dest.set { i, _ -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.vectorElementAtUnsafe(i)) }
            is IdentityMatrix<A>       -> dest.setUnsafe(this)
            is ZeroMatrix<A>           -> dest.set { _, _ -> ring.zero }
            else                       -> dest.set { i, j -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.elementAtUnsafe(i, j)) }
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>             -> dest.setElementAtUnsafe(0u, 0u, ring.multiply(this.vectorElementAtUnsafe(0u), matrix.value))
            is AbstractRowVector<A>    -> dest.setRowParallel { _, j -> ring.multiply(this.vectorElementAtUnsafe(0u), matrix.vectorElementAtUnsafe(j)) }
            is AbstractColumnVector<A> -> dest.setRowParallel { i, _ -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.vectorElementAtUnsafe(i)) }
            is IdentityMatrix<A>       -> dest.setUnsafeRowParallel(this)
            is ZeroMatrix<A>           -> dest.setRowParallel { _, _ -> ring.zero }
            else                       -> dest.setRowParallel { i, j -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.elementAtUnsafe(i, j)) }
        }
    }

    override fun downCast(): AbstractMatrix<A> = when (rows) {
        1u   -> Constant(ring, vectorElementAtUnsafe(0u))
        else -> super.downCast()
    }

    override fun determinant(): A = ring.product(0u until rows) { i -> vectorElementAtUnsafe(i) }

    override fun hasInverse(): Boolean = (0u until rows).all { ring.hasInverse(vectorElementAtUnsafe(it)) }
    override fun inverse(): AbstractSquareMatrix<A> = ring.diagonalMatrix(rows) { i -> ring.inverse(vectorElementAtUnsafe(i)) }

}