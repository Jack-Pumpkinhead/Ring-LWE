package math.martix.concrete

import math.abstract_structure.Ring
import math.martix.AbstractDiagonalMatrix

/**
 * Created by CowardlyLion at 2022/1/17 12:09
 */
class DiagonalMatrix<A>(override val ring: Ring<A>, val diagonal: List<A>) : AbstractDiagonalMatrix<A> {

    override val size: UInt get() = diagonal.size.toUInt()

    override fun vectorElementAtUnsafe(index: UInt): A = diagonal[index.toInt()]

}