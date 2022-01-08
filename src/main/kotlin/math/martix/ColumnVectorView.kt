package math.martix

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/8 16:43
 */
class ColumnVectorView<A>(ring: Ring<A>, val matrix: Matrix<A>, val column: UInt) : AbstractColumnVector<A>(ring, matrix.rows) {

    override fun elementAt(row: UInt, column: UInt): A = matrix.elementAtSafe(row, this.column)


}