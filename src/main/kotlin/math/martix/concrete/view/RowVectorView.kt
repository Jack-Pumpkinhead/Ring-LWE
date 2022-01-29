package math.martix.concrete.view

import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.martix.AbstractRowVector

/**
 * Created by CowardlyLion at 2022/1/8 17:03
 */
class RowVectorView<A>(override val ring: Ring<A>, val matrix: AbstractMatrix<A>, val row: UInt) : AbstractRowVector<A> {

    override val size: UInt get() = matrix.columns

    override fun vectorElementAtUnsafe(index: UInt): A = matrix.elementAt(row, index)

}