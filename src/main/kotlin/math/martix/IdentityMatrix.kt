package math.martix

import math.abstract_structure.CRing
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/9 22:51
 */
class IdentityMatrix<A>(ring: CRing<A>, size: UInt) : AbstractMatrix<A>(ring, size, size) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = if (row == column) ring.one else ring.zero

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = matrix

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = matrix

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        dest.setUnsafe(matrix)
    }

    override suspend fun multiplyToParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        dest.setUnsafe(matrix)
    }
}