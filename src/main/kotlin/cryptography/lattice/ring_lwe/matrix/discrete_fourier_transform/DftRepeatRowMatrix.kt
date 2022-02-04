package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import math.abstract_structure.Ring
import math.martix.AbstractMatrix

/**
 * Created by CowardlyLion at 2022/2/3 22:49
 *
 * repeat row to match [rows]
 */
class DftRepeatRowMatrix<A>(override val ring: Ring<A>, val matrix: List<List<A>>, override val rows: UInt, override val columns: UInt) : AbstractMatrix<A> {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix[row.mod(matrix.size.toUInt()).toInt()][column.toInt()]

}