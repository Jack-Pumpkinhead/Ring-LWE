package math.abstract_structure

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.modular_integer.UIntModular

/**
 * Created by CowardlyLion at 2022/1/7 20:09
 */

/**
 * seen UInt as integer modulo 2^32
 * */
val ringUInt: Ring<UInt> = object : Ring<UInt>(0u, 1u) {
    override fun add(x: UInt, y: UInt): UInt = x + y
    override fun negate(a: UInt): UInt = 0u - a
    override fun multiply(x: UInt, y: UInt): UInt = x * y
}

/**
 * Not optimal for computation, but safe.
 * UInt modulo 0 is empty set thus cannot perform any operation.
 * */
fun ringUIntModulo(modulus: UInt): Ring<UIntModular> = object : Ring<UIntModular>(UIntModular(modulus, 0u), UIntModular(modulus, 1u)) {
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
}


/**
 * Strictly speaking the arithmetic of BigInteger is not a ring (it may overflow (and throwing an error)).
 * */
val ringBigInteger: Ring<BigInteger> = object : Ring<BigInteger>(BigInteger.ZERO, BigInteger.ONE) {
    override fun add(x: BigInteger, y: BigInteger): BigInteger = x + y
    override fun negate(a: BigInteger): BigInteger = -a
    override fun multiply(x: BigInteger, y: BigInteger): BigInteger = x * y
}