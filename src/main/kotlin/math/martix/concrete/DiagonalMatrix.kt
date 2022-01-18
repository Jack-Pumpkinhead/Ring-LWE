package math.martix.concrete

import math.abstract_structure.Ring
import math.martix.AbstractDiagonalMatrix

/**
 * Created by CowardlyLion at 2022/1/17 12:09
 */
class DiagonalMatrix<A>(ring: Ring<A>, val diagonal: List<A>) : AbstractDiagonalMatrix<A>(ring, diagonal.size.toUInt()) {

    override fun vectorElementAt(index: UInt): A = diagonal[index.toInt()]

    override fun vectorElementAtUnsafe(index: UInt): A = diagonal[index.toInt()]


}