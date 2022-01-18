package math.martix.tensor

import math.abstract_structure.Ring
import math.martix.AbstractMatrix

/**
 * Created by CowardlyLion at 2022/1/9 13:35
 */
class SparseSubmatrixView<A>(ring: Ring<A>, val matrix: AbstractMatrix<A>, val rowBase: UInt, val rowSpacing: UInt, rows: UInt) : AbstractMatrix<A>(ring, rows, matrix.columns) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix.elementAt(rowBase + rowSpacing * row, column)

}