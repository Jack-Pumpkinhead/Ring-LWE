package math.abstract_structure.instance

import math.abstract_structure.Ring
import math.bitAt
import math.integer.modInverse
import math.twoPower32

/**
 * Created by CowardlyLion at 2022/1/25 17:34
 *
 * integer modulo 2^32
 */
object RingUInt : Ring<UInt> {
    override val descriptions: MutableSet<String> = mutableSetOf("ring of UInt", "ring of integer modulo 4294967296")
    override val zero: UInt = 0u
    override val one: UInt = 1u

    override fun add(x: UInt, y: UInt): UInt = x + y
    override fun negate(a: UInt): UInt = 0u - a
    override fun subtract(x: UInt, y: UInt): UInt = x - y
    override fun multiply(x: UInt, y: UInt): UInt = x * y

    override fun hasInverse(a: UInt): Boolean = a.bitAt(0u)
    override fun inverse(a: UInt): UInt = a.toULong().modInverse(twoPower32).toUInt()
}