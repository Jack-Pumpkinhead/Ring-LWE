package math.martix.mutable.view

import math.abstract_structure.Ring
import math.martix.mutable.AbstractMutableColumnVector
import math.martix.mutable.AbstractMutableMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/28 21:39
 */
class MutableColumnVectorView<A>(override val ring: Ring<A>, val matrix: AbstractMutableMatrix<A>, val column: UInt) : AbstractMutableColumnVector<A> {

    init {
        lazyAssert2 {
            assert(column < matrix.columns)
        }
    }

    override val size: UInt get() = matrix.rows

    override fun vectorElementAtUnsafe(index: UInt): A = matrix.elementAtUnsafe(index, column)

    override fun setVectorElementAtUnsafe(index: UInt, a: A) {
        matrix.setElementAtUnsafe(index, column, a)
    }

}