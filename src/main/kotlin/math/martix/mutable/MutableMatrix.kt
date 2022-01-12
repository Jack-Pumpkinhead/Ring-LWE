package math.martix.mutable

import math.abstract_structure.CRing
import math.requireEqualSize
import math.sizeOfFirstRowOrZero

/**
 * Created by CowardlyLion at 2022/1/8 22:49
 *
 * Migrate to MutableMatrix due to bad behaviour of ArrayMatrix (cannot create Array without reified type)
 */
class MutableMatrix<A>(ring: CRing<A>, val matrix: List<MutableList<A>>) : AbstractMutableMatrix<A>(ring, matrix.size.toUInt(), sizeOfFirstRowOrZero(matrix)) {

    init {
        requireEqualSize(matrix)
    }


    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix[row.toInt()][column.toInt()]

    override fun setElementAtUnsafe(row: UInt, column: UInt, a: A) {
        matrix[row.toInt()][column.toInt()] = a
    }

}