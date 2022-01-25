package math.abstract_structure.instance

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/25 17:40
 *
 * Strictly speaking the arithmetic of BigInteger is not a ring (it may overflow (and throwing an error)).
 */
object RingBigInteger : Ring<BigInteger> {
    override val descriptions: MutableSet<String> = mutableSetOf("ring of BigInteger")
    override val zero = BigInteger.ZERO
    override val one = BigInteger.ONE

    override fun add(x: BigInteger, y: BigInteger): BigInteger = x + y
    override fun negate(a: BigInteger): BigInteger = -a
    override fun subtract(x: BigInteger, y: BigInteger): BigInteger = x - y
    override fun multiply(x: BigInteger, y: BigInteger): BigInteger = x * y

    override fun hasInverse(a: BigInteger): Boolean = a == BigInteger.ONE || a == BigInteger(-1)

    override fun inverse(a: BigInteger): BigInteger {
        if (hasInverse(a)) {
            return a
        } else error("integer $a has no multiplicative inverse")
    }
}