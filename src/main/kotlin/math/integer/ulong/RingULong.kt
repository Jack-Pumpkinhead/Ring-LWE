package math.integer.ulong

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.Ring
import util.bit.bitAt
import math.twoPower64

/**
 * Created by CowardlyLion at 2022/1/25 17:36
 *
 * integer modulo 2^64
 */
object RingULong : Ring<ULong> {

    override val descriptions: MutableSet<String> = mutableSetOf("ring of ULong", "ring of integer modulo 18446744073709551616")
    override val zero: ULong = 0u
    override val one: ULong = 1u

    override fun add(x: ULong, y: ULong): ULong = x + y
    override fun negate(a: ULong): ULong = 0uL - a
    override fun subtract(x: ULong, y: ULong): ULong = x - y
    override fun multiply(x: ULong, y: ULong): ULong = x * y

    override fun hasInverse(a: ULong): Boolean = a.bitAt(0u)
    override fun inverse(a: ULong): ULong = a.toBigInteger().modInverse(twoPower64).ulongValue()

    override fun ofInteger(a: UInt): ULong = a.toULong()
    override fun ofInteger(a: Int): ULong = a.toULong()
    override fun ofInteger(a: ULong): ULong = a
    override fun ofInteger(a: Long): ULong = a.toULong()

}