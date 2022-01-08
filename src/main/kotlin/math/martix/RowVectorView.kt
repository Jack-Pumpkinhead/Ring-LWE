package math.martix

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/8 17:03
 */
class RowVectorView<A>(ring: Ring<A>, val matrix: Matrix<A>, val row: UInt) : AbstractRowVector<A>(ring, matrix.rows) {

    override fun elementAt(row: UInt, column: UInt): A = matrix.elementAtSafe(this.row, column)

}