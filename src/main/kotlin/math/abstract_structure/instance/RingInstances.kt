package math.abstract_structure.instance

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.modular.ModularBigInteger
import math.abstract_structure.CRing
import math.bitAt
import math.integer.modInverse
import math.modular_integer.UIntModular

/**
 * Created by CowardlyLion at 2022/1/7 20:09
 */

val twoPower32 = 1uL.shl(32)

//integer modulo 2^32
val ringUInt: CRing<UInt> = object : CRing<UInt> {
    override val zero: UInt = 0u
    override val one: UInt = 1u
    override val descriptions: MutableSet<String> = mutableSetOf("ring of UInt", "ring of integer modulo 4294967296")

    override fun add(x: UInt, y: UInt): UInt = x + y
    override fun negate(a: UInt): UInt = 0u - a
    override fun multiply(x: UInt, y: UInt): UInt = x * y

    override fun hasInverse(a: UInt): Boolean = a.bitAt(0u)
    override fun inverse(a: UInt): UInt = a.toULong().modInverse(twoPower32).toUInt()
}

/**
 * Not optimal for computation, but safe.
 * UInt modulo 0 is empty set thus cannot perform any operation.
 * */
fun ringModularUInt(modulus: UInt): CRing<UIntModular> = object : CRing<UIntModular> {
    override val zero: UIntModular = UIntModular(modulus, 0u)
    override val one: UIntModular = UIntModular(modulus, 1u)
    override val descriptions: MutableSet<String> = mutableSetOf("ring of integer modulo $modulus")

    override fun add(x: UIntModular, y: UIntModular): UIntModular {
        require(x.modulus == modulus)
        return x + y
    }

    override fun negate(a: UIntModular): UIntModular {
        require(a.modulus == modulus)
        return UIntModular(a.modulus, 0u) - a
    }

    override fun multiply(x: UIntModular, y: UIntModular): UIntModular {
        require(x.modulus == modulus)
        return x * y
    }

    override fun hasInverse(a: UIntModular): Boolean {
        require(a.modulus == modulus)
        return a.hasInverse()
    }

    override fun inverse(a: UIntModular): UIntModular {
        require(a.modulus == modulus)
        return a.inverse()
    }
}


/**
 * Strictly speaking the arithmetic of BigInteger is not a ring (it may overflow (and throwing an error)).
 * */
val ringBigInteger: CRing<BigInteger> = object : CRing<BigInteger> {
    override val zero = BigInteger.ZERO
    override val one = BigInteger.ONE
    override val descriptions: MutableSet<String> = mutableSetOf("ring of BigInteger")

    override fun add(x: BigInteger, y: BigInteger): BigInteger = x + y
    override fun negate(a: BigInteger): BigInteger = -a
    override fun multiply(x: BigInteger, y: BigInteger): BigInteger = x * y

    override fun hasInverse(a: BigInteger): Boolean = a == BigInteger.ONE || a == BigInteger(-1)

    override fun inverse(a: BigInteger): BigInteger {
        if (hasInverse(a)) {
            return a
        } else error("integer $a has no multiplicative inverse")
    }

}

fun ringModularBigInteger(modulus: BigInteger): CRing<ModularBigInteger> = object : CRing<ModularBigInteger> {
    override val zero = BigInteger.ZERO.toModularBigInteger(modulus)
    override val one = BigInteger.ONE.toModularBigInteger(modulus)
    override val descriptions: MutableSet<String> = mutableSetOf("ring of integer modulo $modulus")

    override fun add(x: ModularBigInteger, y: ModularBigInteger): ModularBigInteger = x + y
    override fun negate(a: ModularBigInteger): ModularBigInteger = -a
    override fun multiply(x: ModularBigInteger, y: ModularBigInteger): ModularBigInteger = x * y

    override fun hasInverse(a: ModularBigInteger): Boolean = a.residue.gcd(a.modulus) == BigInteger.ONE
    override fun inverse(a: ModularBigInteger): ModularBigInteger = a.inverse()
}

