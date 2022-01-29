package math.martix.mutable.view

import math.abstract_structure.Ring
import math.martix.mutable.AbstractMutableMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/19 12:42
 */
class MutableSubmatrixView<A>(override val ring: Ring<A>, val matrix: AbstractMutableMatrix<A>, val rowBase: UInt, val columnBase: UInt, override val rows: UInt, override val columns: UInt) : AbstractMutableMatrix<A> {

    init {
        lazyAssert2 {
            if (rows > 0u) {
                assert(rowBase + rows <= matrix.rows)
            }
            if (columns > 0u) {
                assert(rowBase + columns <= matrix.columns)
            }
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix.elementAtUnsafe(rowBase + row, columnBase + column)

    override fun setElementAtUnsafe(row: UInt, column: UInt, a: A) {
        matrix.setElementAtUnsafe(rowBase + row, columnBase + column, a)
    }


}