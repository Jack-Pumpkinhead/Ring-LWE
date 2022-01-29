package math.martix.concrete

import math.abstract_structure.Ring
import math.martix.AbstractRowVector

/**
 * Created by CowardlyLion at 2022/1/8 14:05
 */
class RowVector<A>(override val ring: Ring<A>, val vector: List<A>) : AbstractRowVector<A> {

    override val size: UInt get() = vector.size.toUInt()

    override fun vectorElementAtUnsafe(index: UInt): A = vector[index.toInt()]


    override fun rowListAt(row: UInt): List<A> {
        require(row == 0u)
        return vector
    }

    override fun rowMutableListAt(row: UInt): MutableList<A> {
        require(row == 0u)
        return vector.toMutableList()
    }

}