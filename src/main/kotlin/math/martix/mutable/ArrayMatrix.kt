package math.martix.mutable

import math.abstract_structure.CRing

/**
 * Created by CowardlyLion at 2022/1/8 22:29
 */
class ArrayMatrix<A>(ring: CRing<A>, val matrix: List<Array<A>>) : AbstractMutableMatrix<A>(ring, matrix.size.toUInt(), if (matrix.isEmpty()) 0u else matrix[0].size.toUInt()) {

    init {
        for (row in matrix) {
            require(row.size.toUInt() == columns)
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix[row.toInt()][column.toInt()]

    override fun setElementAtUnsafe(row: UInt, column: UInt, a: A) {
        matrix[row.toInt()][column.toInt()] = a
    }

}