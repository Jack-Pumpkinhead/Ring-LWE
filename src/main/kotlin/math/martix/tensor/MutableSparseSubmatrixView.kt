package math.martix.tensor

import math.abstract_structure.CRing
import math.martix.mutable.AbstractMutableMatrix
import math.martix.mutable.MutableMatrix

/**
 * Created by CowardlyLion at 2022/1/9 13:52
 */
class MutableSparseSubmatrixView<A>(ring: CRing<A>, val matrix: MutableMatrix<A>, val rowBase: UInt, val rowSpacing: UInt, rows: UInt) : AbstractMutableMatrix<A>(ring, rows, matrix.columns) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix.elementAt(rowBase + rowSpacing * row, column)
    override fun setElementAt(row: UInt, column: UInt, a: A) = matrix.setElementAtSafe(rowBase + rowSpacing * row, column, a)

}