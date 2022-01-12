package math.martix

import math.abstract_structure.CRing
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/11 18:20
 */
class ZeroMatrix<A>(ring: CRing<A>, rows: UInt, columns: UInt) : AbstractMatrix<A>(ring, rows, columns) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = ring.zero

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = ZeroMatrix(ring, this.rows, matrix.columns)

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = ZeroMatrix(ring, this.rows, matrix.columns)

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        dest.setUnsafe(ZeroMatrix(ring, this.rows, matrix.columns))
    }

    override suspend fun multiplyToParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        dest.setUnsafe(ZeroMatrix(ring, this.rows, matrix.columns))
    }
}