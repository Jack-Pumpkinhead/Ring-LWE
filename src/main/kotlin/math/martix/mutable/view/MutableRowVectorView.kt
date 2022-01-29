package math.martix.mutable.view

import math.abstract_structure.Ring
import math.martix.mutable.AbstractMutableMatrix
import math.martix.mutable.AbstractMutableRowVector
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/28 21:46
 */
class MutableRowVectorView<A>(override val ring: Ring<A>, val matrix: AbstractMutableMatrix<A>, val row: UInt) : AbstractMutableRowVector<A> {

    init {
        lazyAssert2 {
            assert(row < matrix.rows)
        }
    }

    override val size: UInt get() = matrix.columns

    override fun vectorElementAtUnsafe(index: UInt): A = matrix.elementAtUnsafe(row, index)

    override fun setVectorElementAtUnsafe(index: UInt, a: A) {
        matrix.setElementAtUnsafe(row, index, a)
    }

}