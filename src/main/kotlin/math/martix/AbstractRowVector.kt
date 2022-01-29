package math.martix

import math.martix.concrete.Constant
import math.martix.mutable.AbstractMutableMatrix
import math.operation.innerProduct
import math.vector.VectorLike

/**
 * Created by CowardlyLion at 2022/1/8 17:03
 */
interface AbstractRowVector<A> : AbstractMatrix<A>, VectorLike<A> {

    override val rows: UInt get() = 1u
    override val columns: UInt get() = size

    override fun elementAtUnsafe(row: UInt, column: UInt): A = vectorElementAtUnsafe(column)

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> =
        when (matrix) {
            is Constant<A>             -> Constant(ring, ring.multiply(vectorElementAtUnsafe(0u), matrix.value))    //1->1->1
            is AbstractColumnVector<A> -> Constant(ring, ring.innerProduct(this, matrix)) //1->a->1
            else                       -> super.timesImpl(matrix)   //1->a->b
        }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> =
        when (matrix) {
            is Constant<A>             -> Constant(ring, ring.multiply(vectorElementAtUnsafe(0u), matrix.value))    //1->1->1
            is AbstractColumnVector<A> -> Constant(ring, ring.innerProduct(this, matrix)) //1->a->1
            else                       -> super.timesRowParallelImpl(matrix)   //1->a->b
        }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>             -> dest.setElementAt(0u, 0u, ring.multiply(vectorElementAtUnsafe(0u), matrix.value))    //1->1->1
            is AbstractColumnVector<A> -> dest.setElementAt(0u, 0u, ring.innerProduct(this, matrix)) //1->a->1
            else                       -> super.multiplyToImpl(matrix, dest)   //1->a->b
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>             -> dest.setElementAt(0u, 0u, ring.multiply(vectorElementAtUnsafe(0u), matrix.value))    //1->1->1
            is AbstractColumnVector<A> -> dest.setElementAt(0u, 0u, ring.innerProduct(this, matrix)) //1->a->1
            else                       -> super.multiplyToRowParallelImpl(matrix, dest)   //1->a->b
        }
    }
}