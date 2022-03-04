package math.integer.long

import math.abstract_structure.Ring
import kotlin.math.absoluteValue

/**
 * Created by CowardlyLion at 2022/2/25 20:27
 *
 * 'ring' of Long
 *
 * only safe when operates on a small integers
 */
object RingLong : Ring<Long> {

    override val descriptions: MutableSet<String> = mutableSetOf("ring of Long")
    override val zero: Long = 0L
    override val one: Long = 1L

    override fun add(x: Long, y: Long): Long = x + y
    override fun negate(a: Long): Long = -a
    override fun subtract(x: Long, y: Long): Long = x - y
    override fun multiply(x: Long, y: Long): Long = x * y

    override fun hasInverse(a: Long): Boolean = a.absoluteValue == 1L
    override fun inverse(a: Long): Long {
        require(hasInverse(a))
        return a
    }

    override fun ofInteger(a: UInt): Long = a.toLong()
    override fun ofInteger(a: Int): Long = a.toLong()
    override fun ofInteger(a: ULong): Long {
        require(a <= Long.MAX_VALUE.toULong())
        return a.toLong()
    }
    override fun ofInteger(a: Long): Long = a

    override val isExactComputation: Boolean get() = true   //require to be true, no overflow should occur.
}