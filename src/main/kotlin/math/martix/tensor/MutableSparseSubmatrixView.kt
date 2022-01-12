package math.martix.tensor

import math.abstract_structure.CRing
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/9 13:52
 * represent a view of matrix at rows {rowBase + rowSpacing * i, i < rows}
 */
class MutableSparseSubmatrixView<A>(ring: CRing<A>, val matrix: AbstractMutableMatrix<A>, val rowBase: UInt, val rowSpacing: UInt, rows: UInt) : AbstractMutableMatrix<A>(ring, rows, matrix.columns) {

    init {
        if (rows != 0u) {
            require(rowBase + rowSpacing * (rows - 1u) < matrix.rows)
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix.elementAt(rowBase + rowSpacing * row, column)
    override fun setElementAtUnsafe(row: UInt, column: UInt, a: A) = matrix.setElementAtUnsafe(rowBase + rowSpacing * row, column, a)

}