package math.martix.mutable

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/28 17:45
 */
class MutableRowVector<A>(override val ring: Ring<A>, val vector: MutableList<A>) : AbstractMutableRowVector<A> {

    override val size: UInt get() = vector.size.toUInt()

    override fun vectorElementAtUnsafe(index: UInt): A = vector[index.toInt()]

    override fun setVectorElementAtUnsafe(index: UInt, a: A) {
        vector[index.toInt()] = a
    }

}