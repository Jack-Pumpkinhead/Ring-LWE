package math.martix.mutable

import math.abstract_structure.Ring
import math.isRectangular
import math.sizeOfFirstRowOrZero
import util.stdlib.lazyAssert

/**
 * Created by CowardlyLion at 2022/1/8 22:49
 *
 * Migrate to MutableMatrix due to bad behaviour of ArrayMatrix (cannot create Array without reified type)
 */
class MutableMatrix<A>(ring: Ring<A>, val matrix: List<MutableList<A>>) : AbstractMutableMatrix<A>(ring, matrix.size.toUInt(), sizeOfFirstRowOrZero(matrix)) {

    init {
        lazyAssert { isRectangular(matrix) }
    }


    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix[row.toInt()][column.toInt()]

    override fun setElementAtUnsafe(row: UInt, column: UInt, a: A) {
        matrix[row.toInt()][column.toInt()] = a
    }

    override fun rowListAt(row: UInt): List<A> {
        return matrix[row.toInt()].toList()
    }

    override fun rowMutableListAt(row: UInt): MutableList<A> {
        return matrix[row.toInt()].toMutableList()
    }
}