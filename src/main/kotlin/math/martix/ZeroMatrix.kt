package math.martix

import math.abstract_structure.Ring
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/11 18:20
 */
class ZeroMatrix<A>(override val ring: Ring<A>, override val rows: UInt, override val columns: UInt) : AbstractMatrix<A> {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = ring.zero

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = ZeroMatrix(ring, this.rows, matrix.columns)

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = ZeroMatrix(ring, this.rows, matrix.columns)

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        dest.set { _, _ -> ring.zero }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        dest.setRowParallel { _, _ -> ring.zero }
    }

}