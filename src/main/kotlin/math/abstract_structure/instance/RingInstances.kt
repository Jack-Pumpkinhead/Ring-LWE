package math.abstract_structure.instance

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.modular.ModularBigInteger
import math.abstract_structure.CRing
import math.modular_integer.UIntModular

/**
 * Created by CowardlyLion at 2022/1/7 20:09
 */

/**
 * seen UInt as integer modulo 2^32
 * */

//integer modulo 2^32
val ringUInt: CRing<UInt> = object : CRing<UInt>(mutableSetOf("ring of UInt", "ring of integer modulo 4294967296"), 0u, 1u) {
    override fun add(x: UInt, y: UInt): UInt = x + y
    override fun negate(a: UInt): UInt = 0u - a
    override fun multiply(x: UInt, y: UInt): UInt = x * y
}

/**
 * Not optimal for computation, but safe.
 * UInt modulo 0 is empty set thus cannot perform any operation.
 * */
fun ringModularUInt(modulus: UInt): CRing<UIntModular> = object : CRing<UIntModular>(mutableSetOf("ring of integer modulo $modulus"), UIntModular(modulus, 0u), UIntModular(modulus, 1u)) {
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
val ringBigInteger: CRing<BigInteger> = object : CRing<BigInteger>(mutableSetOf("ring of BigInteger"), BigInteger.ZERO, BigInteger.ONE) {
    override fun add(x: BigInteger, y: BigInteger): BigInteger = x + y
    override fun negate(a: BigInteger): BigInteger = -a
    override fun multiply(x: BigInteger, y: BigInteger): BigInteger = x * y
}

fun ringModularBigInteger(modulus: BigInteger): CRing<ModularBigInteger> = object : CRing<ModularBigInteger>(mutableSetOf("ring of integer modulo $modulus"), BigInteger.ZERO.toModularBigInteger(modulus), BigInteger.ONE.toModularBigInteger(modulus)) {
    override fun add(x: ModularBigInteger, y: ModularBigInteger): ModularBigInteger = x + y
    override fun negate(a: ModularBigInteger): ModularBigInteger = -a
    override fun multiply(x: ModularBigInteger, y: ModularBigInteger): ModularBigInteger = x * y
}

