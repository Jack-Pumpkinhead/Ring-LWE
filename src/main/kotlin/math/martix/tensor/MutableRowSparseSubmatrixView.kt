package math.martix.tensor

import math.abstract_structure.Ring
import math.martix.mutable.AbstractMutableMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/9 13:52
 * represent a view of matrix at rows {rowBase + rowSpacing * i, i < rows}
 */
class MutableRowSparseSubmatrixView<A>(override val ring: Ring<A>, val matrix: AbstractMutableMatrix<A>, val rowBase: UInt, val rowSpacing: UInt, override val rows: UInt) : AbstractMutableMatrix<A> {

    override val columns: UInt get() = matrix.columns

    init {
        lazyAssert2 {
            if (rows != 0u) {
                assert(rowBase + rowSpacing * (rows - 1u) < matrix.rows)
            }
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix.elementAtUnsafe(rowBase + rowSpacing * row, column)

    override fun setElementAtUnsafe(row: UInt, column: UInt, a: A) {
        matrix.setElementAtUnsafe(rowBase + rowSpacing * row, column, a)
    }

}