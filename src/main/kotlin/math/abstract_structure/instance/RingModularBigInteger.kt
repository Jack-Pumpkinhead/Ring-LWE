package math.abstract_structure.instance

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import com.ionspin.kotlin.bignum.modular.ModularBigInteger
import math.abstract_structure.Ring
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/25 17:41
 */
class RingModularBigInteger(val modulus: BigInteger) : Ring<ModularBigInteger> {

    init {
        lazyAssert2 {
            assert(modulus.isPositive)
        }
    }

    override val descriptions: MutableSet<String> = mutableSetOf("ring of integer modulo $modulus")
    override val zero = BigInteger.ZERO.toModularBigInteger(modulus)
    override val one = BigInteger.ONE.toModularBigInteger(modulus)

    override fun add(x: ModularBigInteger, y: ModularBigInteger): ModularBigInteger = x + y
    override fun negate(a: ModularBigInteger): ModularBigInteger = -a
    override fun subtract(x: ModularBigInteger, y: ModularBigInteger): ModularBigInteger = x - y
    override fun multiply(x: ModularBigInteger, y: ModularBigInteger): ModularBigInteger = x * y

    override fun hasInverse(a: ModularBigInteger): Boolean = a.residue.gcd(a.modulus) == BigInteger.ONE
    override fun inverse(a: ModularBigInteger): ModularBigInteger = a.inverse()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RingModularBigInteger) return false

        if (modulus != other.modulus) return false

        return true
    }

    override fun hashCode(): Int {
        return modulus.hashCode()
    }

    override fun ofInteger(a: UInt): ModularBigInteger = a.toBigInteger().mod(modulus).toModularBigInteger(modulus)
    override fun ofInteger(a: Int): ModularBigInteger = a.toBigInteger().mod(modulus).toModularBigInteger(modulus)
    override fun ofInteger(a: ULong): ModularBigInteger = a.toBigInteger().mod(modulus).toModularBigInteger(modulus)
    override fun ofInteger(a: Long): ModularBigInteger = a.toBigInteger().mod(modulus).toModularBigInteger(modulus)


}