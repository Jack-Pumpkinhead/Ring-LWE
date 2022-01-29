package math.martix.concrete

import math.abstract_structure.Ring
import math.martix.*

/**
 * Created by CowardlyLion at 2022/1/8 14:00
 */
class ColumnVector<A>(override val ring: Ring<A>, val vector: List<A>) : AbstractColumnVector<A> {

    override val size: UInt get() = vector.size.toUInt()

    override fun vectorElementAtUnsafe(index: UInt): A = vector[index.toInt()]

}