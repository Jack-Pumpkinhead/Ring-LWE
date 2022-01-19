package math.abstract_structure.instance

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import com.ionspin.kotlin.bignum.modular.ModularBigInteger
import math.abstract_structure.Ring
import math.bitAt
import math.complex_number.ComplexNumber
import math.complex_number.complexNumber
import math.integer.modInverse
import math.integer.modular.UIntModular

/**
 * Created by CowardlyLion at 2022/1/7 20:09
 */

val twoPower32 = 1uL.shl(32)
val twoPower64 = BigInteger.ONE.shl(64)

//integer modulo 2^32
val ringUInt: Ring<UInt> = object : Ring<UInt> {
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

//integer modulo 2^64
val ringULong: Ring<ULong> = object : Ring<ULong> {
    override val descriptions: MutableSet<String> = mutableSetOf("ring of ULong", "ring of integer modulo 18446744073709551616")
    override val zero: ULong = 0u
    override val one: ULong = 1u

    override fun add(x: ULong, y: ULong): ULong = x + y
    override fun negate(a: ULong): ULong = 0uL - a
    override fun subtract(x: ULong, y: ULong): ULong = x - y
    override fun multiply(x: ULong, y: ULong): ULong = x * y

    override fun hasInverse(a: ULong): Boolean = a.bitAt(0u)
    override fun inverse(a: ULong): ULong = a.toBigInteger().modInverse(twoPower64).ulongValue()
}


/**
 * Not optimal for computation, but safe.
 * UInt modulo 0 is empty set thus cannot perform any operation.
 * */
fun ringModularUInt(modulus: UInt): Ring<UIntModular> = object : Ring<UIntModular> {
    override val descriptions: MutableSet<String> = mutableSetOf("ring of integer modulo $modulus")
    override val zero: UIntModular = UIntModular(modulus, 0u)
    override val one: UIntModular = UIntModular(modulus, 1u)

    override fun add(x: UIntModular, y: UIntModular): UIntModular {
        require(x.modulus == modulus)
        return x + y    //already checking modulus of x and y equal
    }

    override fun negate(a: UIntModular): UIntModular {
        require(a.modulus == modulus)
        return -a
    }

    override fun subtract(x: UIntModular, y: UIntModular): UIntModular {
        require(x.modulus == modulus)
        return x - y
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
val ringBigInteger: Ring<BigInteger> = object : Ring<BigInteger> {
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

fun ringModularBigInteger(modulus: BigInteger): Ring<ModularBigInteger> = object : Ring<ModularBigInteger> {
    override val descriptions: MutableSet<String> = mutableSetOf("ring of integer modulo $modulus")
    override val zero = BigInteger.ZERO.toModularBigInteger(modulus)
    override val one = BigInteger.ONE.toModularBigInteger(modulus)

    override fun add(x: ModularBigInteger, y: ModularBigInteger): ModularBigInteger = x + y
    override fun negate(a: ModularBigInteger): ModularBigInteger = -a
    override fun subtract(x: ModularBigInteger, y: ModularBigInteger): ModularBigInteger = x - y
    override fun multiply(x: ModularBigInteger, y: ModularBigInteger): ModularBigInteger = x * y

    override fun hasInverse(a: ModularBigInteger): Boolean = a.residue.gcd(a.modulus) == BigInteger.ONE
    override fun inverse(a: ModularBigInteger): ModularBigInteger = a.inverse()
}

fun <A> Ring<A>.ringComplexNumber(): Ring<ComplexNumber<A>> = object : Ring<ComplexNumber<A>> {
    override val descriptions: MutableSet<String> = this@ringComplexNumber.descriptions.mapTo(mutableSetOf()) { "R[x]/(x^2+1) over ($it)" }
    override val zero: ComplexNumber<A> = this@ringComplexNumber.complexNumber(this@ringComplexNumber.zero, this@ringComplexNumber.zero)
    override val one: ComplexNumber<A> = this@ringComplexNumber.complexNumber(this@ringComplexNumber.one, this@ringComplexNumber.zero)

    override fun add(x: ComplexNumber<A>, y: ComplexNumber<A>): ComplexNumber<A> {
        require(this@ringComplexNumber == x.ring)
        return x + y    //already checking ring of x and y equal here.
    }

    override fun negate(a: ComplexNumber<A>): ComplexNumber<A> {
        require(this@ringComplexNumber == a.ring)
        return -a
    }

    override fun subtract(x: ComplexNumber<A>, y: ComplexNumber<A>): ComplexNumber<A> {
        require(this@ringComplexNumber == x.ring)
        return x - y
    }

    override fun multiply(x: ComplexNumber<A>, y: ComplexNumber<A>): ComplexNumber<A> {
        require(this@ringComplexNumber == x.ring)
        return x * y
    }

    override fun hasInverse(a: ComplexNumber<A>): Boolean {
        require(this@ringComplexNumber == a.ring)
        return a.hasInverse()
    }

    override fun inverse(a: ComplexNumber<A>): ComplexNumber<A> {
        require(this@ringComplexNumber == a.ring)
        return a.inverse()
    }

}