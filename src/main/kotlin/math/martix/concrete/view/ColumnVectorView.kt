package math.martix.concrete.view

import math.abstract_structure.Ring
import math.martix.AbstractColumnVector
import math.martix.AbstractMatrix

/**
 * Created by CowardlyLion at 2022/1/8 16:43
 */
class ColumnVectorView<A>(ring: Ring<A>, val matrix: AbstractMatrix<A>, val column: UInt) : AbstractColumnVector<A>(ring, matrix.rows) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix.elementAt(row, this.column)

}