package math.martix.mutable

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/28 17:14
 */
class MutableColumnVector<A>(override val ring: Ring<A>, val vector: MutableList<A>) : AbstractMutableColumnVector<A> {

    override val size: UInt get() = vector.size.toUInt()

    override fun vectorElementAtUnsafe(index: UInt): A = vector[index.toInt()]

    override fun setVectorElementAtUnsafe(index: UInt, a: A) {
        vector[index.toInt()] = a
    }

    override fun columnListAt(column: UInt): List<A> {
        require(column == 0u)
        return vector.toList()
    }

    override fun columnMutableListAt(column: UInt): MutableList<A> {
        require(column == 0u)
        return vector.toMutableList()
    }

}