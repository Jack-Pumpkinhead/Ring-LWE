package math.martix.concrete.view

import math.abstract_structure.Ring
import math.martix.AbstractColumnVector
import math.martix.AbstractMatrix

/**
 * Created by CowardlyLion at 2022/1/8 16:43
 */
class ColumnVectorView<A>(override val ring: Ring<A>, val matrix: AbstractMatrix<A>, val column: UInt) : AbstractColumnVector<A> {

    override val size: UInt get() = matrix.rows

    override fun vectorElementAtUnsafe(index: UInt): A = matrix.elementAt(index, column)

}