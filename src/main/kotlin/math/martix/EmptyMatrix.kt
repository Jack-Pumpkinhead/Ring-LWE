package math.martix

import math.abstract_structure.Ring
import util.stdlib.lazyAssert

/**
 * Created by CowardlyLion at 2022/1/9 15:02
 */
class EmptyMatrix<A>(override val ring: Ring<A>, override val rows: UInt, override val columns: UInt) : AbstractMatrix<A> {

    init {
        lazyAssert { rows == 0u || columns == 0u }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        error("empty matrix have no elements.")
    }

}