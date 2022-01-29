package cryptography.lattice.ring_lwe.matrix

import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.IdentityMatrix
import math.martix.concrete.OrdinaryMatrix
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/27 17:12
 */
class LowerTriangularOneMatrix<A>(override val ring: Ring<A>, override val size: UInt) : AbstractSquareMatrix<A> {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = if (column <= row) ring.one else ring.zero

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> =
        if (size == 1u) {
            matrix
        } else {
            val lists = mutableListOf<List<A>>()
            val row = matrix.rowMutableListAt(0u)
            lists += row.toList()
            for (i in 1u until matrix.rows) {
                for (j in 0u until matrix.columns) {
                    row[j.toInt()] = ring.add(row[j.toInt()], matrix.elementAtUnsafe(i, j))
                }
                lists += row.toList()
            }
            OrdinaryMatrix(ring, size, matrix.columns,lists)
        }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = timesImpl(matrix)

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        if (this.rows == 1u) {
            dest.setUnsafe(matrix)
        } else {
            val row = matrix.rowMutableListAt(0u)
            dest.setRowUnsafe(0u, row)
            for (i in 1u until matrix.rows) {
                for (j in 0u until matrix.columns) {
                    row[j.toInt()] = ring.add(row[j.toInt()], matrix.elementAtUnsafe(i, j))
                }
                dest.setRowUnsafe(i, row)
            }
        }
    }

    // no way for row parallel, but column parallel is possible
    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) = multiplyToImpl(matrix, dest)

    override fun downCast(): AbstractMatrix<A> {
        return if (size == 1u) IdentityMatrix(ring, 1u)
        else super.downCast()
    }

    override fun determinant(): A = ring.one

    override fun hasInverse(): Boolean = true

    override fun inverse(): AbstractSquareMatrix<A> = InverseLowerTriangularOneMatrix(ring, size)
}