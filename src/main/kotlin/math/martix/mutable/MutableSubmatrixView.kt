package math.martix.mutable

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/19 12:42
 */
class MutableSubmatrixView<A>(ring: Ring<A>, val matrix: AbstractMutableMatrix<A>, val rowBase: UInt, val columnBase: UInt, rows: UInt, columns: UInt) : AbstractMutableMatrix<A>(ring, rows, columns) {

    init {
        if (rows > 0u) {
            require(rowBase + rows <= matrix.rows)
        }
        if (columns > 0u) {
            require(rowBase + columns <= matrix.columns)
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix.elementAtUnsafe(rowBase + row, columnBase + column)

    override fun setElementAtUnsafe(row: UInt, column: UInt, a: A) {
        matrix.setElementAtUnsafe(rowBase + row, columnBase + column, a)
    }


}