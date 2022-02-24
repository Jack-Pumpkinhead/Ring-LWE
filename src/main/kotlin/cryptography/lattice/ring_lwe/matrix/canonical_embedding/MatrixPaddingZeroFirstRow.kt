package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import math.abstract_structure.Ring
import math.martix.AbstractMatrix

/**
 * Created by CowardlyLion at 2022/2/24 18:40
 */
class MatrixPaddingZeroFirstRow<A>(val matrix: AbstractMatrix<A>) : AbstractMatrix<A> {

    override val ring: Ring<A>
        get() = matrix.ring
    override val rows: UInt
        get() = matrix.rows + 1u
    override val columns: UInt
        get() = matrix.columns

    override fun elementAtUnsafe(row: UInt, column: UInt): A =
        if (row == 0u) {
            ring.zero
        } else {
            matrix.elementAtUnsafe(row - 1u, column)
        }

}