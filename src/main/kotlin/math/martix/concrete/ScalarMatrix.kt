package math.martix.concrete

import math.abstract_structure.Ring
import math.martix.AbstractDiagonalMatrix

/**
 * Created by CowardlyLion at 2022/1/28 16:49
 */
open class ScalarMatrix<A>(override val ring: Ring<A>, override val size: UInt, val value: A) : AbstractDiagonalMatrix<A> {

    override fun vectorElementAtUnsafe(index: UInt): A = value

}