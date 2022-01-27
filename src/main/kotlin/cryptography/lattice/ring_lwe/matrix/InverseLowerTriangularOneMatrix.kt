package cryptography.lattice.ring_lwe.matrix

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.IdentityMatrix
import math.martix.concrete.OrdinaryMatrix
import math.martix.mutable.AbstractMutableMatrix
import util.stdlib.list

/**
 * Created by CowardlyLion at 2022/1/27 17:45
 */
class InverseLowerTriangularOneMatrix<A>(ring: Ring<A>, size: UInt) : AbstractSquareMatrix<A>(ring, size) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = when (row) {
        column      -> ring.one
        column + 1u -> ring.negate(ring.one)
        else        -> ring.zero
    }

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> =
        if (this.rows == 1u) {
            matrix
        } else {
            val lists = mutableListOf<List<A>>()
            lists += matrix.rowListAt(0u)

            for (i in 1u until matrix.rows) {
                lists += list(matrix.columns) { j -> ring.subtract(matrix.elementAtUnsafe(i, j), matrix.elementAtUnsafe(i - 1u, j)) }
            }
            OrdinaryMatrix(ring, lists)
        }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = coroutineScope {
        if (this@InverseLowerTriangularOneMatrix.rows == 1u) {
            matrix
        } else {
            val lists = list(matrix.rows) { i ->
                async {
                    if (i == 0u) {
                        matrix.rowListAt(i)
                    } else {
                        list(matrix.columns) { j -> ring.subtract(matrix.elementAtUnsafe(i, j), matrix.elementAtUnsafe(i - 1u, j)) }
                    }
                }
            }.awaitAll()
            OrdinaryMatrix(ring, lists)
        }
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        if (this.rows == 1u) {
            dest.setUnsafe(matrix)
        } else {
            dest.setRowUnsafe(0u, matrix.rowVectorViewAt(0u))
            for (i in 1u until matrix.rows) {
                dest.setRowUnsafe(i) { j -> ring.subtract(matrix.elementAtUnsafe(i, j), matrix.elementAtUnsafe(i - 1u, j)) }
            }
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) = coroutineScope {
        if (this@InverseLowerTriangularOneMatrix.rows == 1u) {
            dest.setUnsafe(matrix)
        } else {
            dest.setRowUnsafe(0u, matrix.rowVectorViewAt(0u))
            for (i in 1u until matrix.rows) {
                launch {
                    dest.setRowUnsafe(i) { j -> ring.subtract(matrix.elementAtUnsafe(i, j), matrix.elementAtUnsafe(i - 1u, j)) }
                }
            }
        }
    }

    override fun downCast(): AbstractMatrix<A> {
        return if (rows == 1u) IdentityMatrix(ring, 1u)
        else super.downCast()
    }

    override fun determinant(): A = ring.one

    override fun hasInverse(): Boolean = true

    override fun inverse(): AbstractSquareMatrix<A> = LowerTriangularOneMatrix(ring, rows)

}
