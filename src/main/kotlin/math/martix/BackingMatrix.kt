package math.martix

import math.abstract_structure.Ring
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/29 22:31
 *
 * not all methods are delegated
 */
interface BackingMatrix<A> : AbstractMatrix<A> {

    val underlyingMatrix: AbstractMatrix<A>

    override val ring: Ring<A> get() = underlyingMatrix.ring
    override val rows: UInt get() = underlyingMatrix.rows
    override val columns: UInt get() = underlyingMatrix.columns

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = underlyingMatrix.timesImpl(matrix)

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = underlyingMatrix.timesRowParallelImpl(matrix)

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        underlyingMatrix.multiplyToImpl(matrix, dest)
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        underlyingMatrix.multiplyToRowParallelImpl(matrix, dest)
    }

}