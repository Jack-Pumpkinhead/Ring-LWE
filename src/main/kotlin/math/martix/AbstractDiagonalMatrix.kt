package math.martix

import math.abstract_structure.Ring
import math.martix.concrete.Constant
import math.martix.mutable.AbstractMutableMatrix
import math.operations.product

/**
 * Created by CowardlyLion at 2022/1/17 11:54
 */
abstract class AbstractDiagonalMatrix<A>(ring: Ring<A>, size: UInt) : AbstractMatrix<A>(ring, size, size), VectorLike<A> {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = if (row != column) ring.zero else vectorElementAtUnsafe(row)

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is Constant<A>             -> Constant(ring, ring.multiply(this.vectorElementAtUnsafe(0u), matrix.value))
        is AbstractRowVector<A>    -> ring.rowVector(matrix.columns) { i -> ring.multiply(this.vectorElementAtUnsafe(0u), matrix.vectorElementAtUnsafe(i)) }
        is AbstractColumnVector<A> -> ring.columnVector(matrix.rows) { i -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.vectorElementAtUnsafe(i)) }
        is IdentityMatrix<A>       -> this
        is ZeroMatrix<A>           -> ZeroMatrix(ring, this.rows, matrix.columns)
        else                       -> ring.matrix(matrix.rows, matrix.columns) { i, j -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.elementAtUnsafe(i, j)) }
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is Constant<A>             -> Constant(ring, ring.multiply(this.vectorElementAtUnsafe(0u), matrix.value))
        is AbstractRowVector<A>    -> ring.rowVector(matrix.columns) { i -> ring.multiply(this.vectorElementAtUnsafe(0u), matrix.vectorElementAtUnsafe(i)) }
        is AbstractColumnVector<A> -> ring.columnVectorParallel(matrix.rows) { i -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.vectorElementAtUnsafe(i)) }
        is IdentityMatrix<A>       -> this
        is ZeroMatrix<A>           -> ZeroMatrix(ring, this.rows, matrix.columns)
        else                       -> ring.matrixRowParallel(matrix.rows, matrix.columns) { i, j -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.elementAtUnsafe(i, j)) }
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>             -> dest.setElementAtUnsafe(0u, 0u, ring.multiply(this.vectorElementAtUnsafe(0u), matrix.value))
            is AbstractRowVector<A>    -> dest.indexedSet { _, j -> ring.multiply(this.vectorElementAtUnsafe(0u), matrix.vectorElementAtUnsafe(j)) }
            is AbstractColumnVector<A> -> dest.indexedSet { i, _ -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.vectorElementAtUnsafe(i)) }
            is IdentityMatrix<A>       -> dest.setUnsafe(this)
            is ZeroMatrix<A>           -> dest.indexedSet { _, _ -> ring.zero }
            else                       -> dest.indexedSet { i, j -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.elementAtUnsafe(i, j)) }
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>             -> dest.setElementAtUnsafe(0u, 0u, ring.multiply(this.vectorElementAtUnsafe(0u), matrix.value))
            is AbstractRowVector<A>    -> dest.indexedSetRowParallel { _, j -> ring.multiply(this.vectorElementAtUnsafe(0u), matrix.vectorElementAtUnsafe(j)) }
            is AbstractColumnVector<A> -> dest.indexedSetRowParallel { i, _ -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.vectorElementAtUnsafe(i)) }
            is IdentityMatrix<A>       -> dest.setUnsafeRowParallel(this)
            is ZeroMatrix<A>           -> dest.indexedSetRowParallel { _, _ -> ring.zero }
            else                       -> dest.indexedSetRowParallel { i, j -> ring.multiply(this.vectorElementAtUnsafe(i), matrix.elementAtUnsafe(i, j)) }
        }
    }

    override fun downCast(): AbstractMatrix<A> = when (rows) {
        1u   -> Constant(ring, vectorElementAtUnsafe(0u))
        else -> super.downCast()
    }

    override fun determinant(): A = ring.product(0u until rows) { i -> vectorElementAtUnsafe(i) }

    override fun hasInverse(): Boolean = (0u until rows).all { ring.hasInverse(vectorElementAtUnsafe(it)) }
    override fun inverse(): AbstractMatrix<A> = ring.diagonalMatrix(rows) { i -> ring.inverse(vectorElementAtUnsafe(i)) }

}