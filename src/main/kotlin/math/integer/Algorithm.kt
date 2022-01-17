package math.integer

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.cache.primeOf
import kotlin.math.absoluteValue
import kotlin.math.sqrt

/**
 * Created by CowardlyLion at 2022/1/14 20:35
 */
/***
 *  may throw error if input is Long.MIN_VALUE
 * */

suspend fun Long.isPrime(): Boolean {
    return when (absoluteValue) {
        0L   -> false
        1L   -> false
        2L   -> true
        3L   -> true
        else -> {
            val sqrt = sqrt(absoluteValue.toDouble()).toLong() + 1
            var i = 0
            var prime = primeOf(i)
            while (prime < sqrt) {  //p*p may overflow.
                if (this % prime == 0L) {
                    return false
                }
                i++
                prime = primeOf(i)
            }
            true
        }
    }
}

class PrimePower(val prime: Long, val power: Int) {
    override fun toString(): String {
        return "$prime^$power"
    }
}

/**
 * since primeOf(Int.MAX_VALUE)^2 - Long.MAX_VALUE = 50685770167^2 - 9223372036854775807 = 2559823925385092432082 > 0,
 * it's ok to test primality of x by testing prime divisor less than sqrt(x).
 * But direct factorization by searching divisor within ("small" primes) primeOf(0...Int.MAX_VALUE) may not complete due to inaccessibility of large prime numbers.
 * A correct implementation should clear out divisor within small primes, then x must be a large prime otherwise it would have a small prime divisor.
 * Realistically one may not calculate prime factorization of large Long number in a reasonably short time by this method.
 * */
suspend fun Long.positivePrimeFactorization(): List<PrimePower> {
    require(this > 0L) { "Not a positive number" }
    return when (this) {
        1L   -> emptyList()
        2L   -> listOf(PrimePower(2L, 1))
        3L   -> listOf(PrimePower(3L, 1))
        else -> {
            val list = mutableListOf<PrimePower>()
            var x = this
            val sqrt = sqrt(absoluteValue.toDouble()).toLong() + 1
            var i = 0
            var prime = primeOf(i)
            while (prime < sqrt) {
                if (this % prime == 0L) {
                    var power = 1
                    x /= prime
                    while (x % prime == 0L) {
                        power++
                        x /= prime
                    }
                    list += PrimePower(prime, power)
                    if (x == 1L) return list
                }
                i++
                prime = primeOf(i)
            }
            list += PrimePower(x, 1)
            list
        }
    }
}

suspend fun Long.radical(): Long {
    require(this > 0L) { "Not a positive number" }
    return when (this) {
        1L   -> 1L
        2L   -> 1L
        3L   -> 2L
        else -> {
            var radical = 1L
            var x = this
            val sqrt = sqrt(absoluteValue.toDouble()).toLong() + 1
            var i = 0
            var prime = primeOf(i)
            while (prime < sqrt) {
                if (this % prime == 0L) {
                    x /= prime
                    while (x % prime == 0L) {
                        x /= prime
                    }
                    radical *= prime
                    if (x == 1L) return radical
                }
                i++
                prime = primeOf(i)
            }
            radical *= x    //see comment at positivePrimeFactorization()
            radical
        }
    }
}

suspend fun Long.eulerTotient(): Long {
    require(this > 0L) { "Not a positive number" }
    return when (this) {
        1L   -> 1L
        2L   -> 1L
        3L   -> 2L
        else -> {
            var eulerTotient = this
            var x = this
            val sqrt = sqrt(absoluteValue.toDouble()).toLong() + 1
            var i = 0
            var prime = primeOf(i)
            while (prime < sqrt) {
                if (this % prime == 0L) {
                    x /= prime
                    while (x % prime == 0L) {
                        x /= prime
                    }
                    eulerTotient = (eulerTotient / prime) * (prime - 1)
                    if (x == 1L) return eulerTotient
                }
                i++
                prime = primeOf(i)
            }
            eulerTotient = (eulerTotient / x) * (x - 1)     //see comment at positivePrimeFactorization()
            eulerTotient
        }
    }
}

fun gcd(a: UInt, b: UInt): UInt {
    var a0 = a
    var a1 = b
    while (a1 != 0u) {
        val q = a0 / a1
        val ta0 = a0
        a0 = a1
        a1 = ta0 - q * a1
    }
    return a0
}

data class ExtendedGCDResultUInt(val r: Long, val s: Long, val gcd: UInt)

/**
 * r*a + s*b = gcd(a,b)
 * */
fun extendedGCD(a: UInt, b: UInt): ExtendedGCDResultUInt {
    var a0 = a
    var a1 = b
    var a00 = 1L
    var a01 = 0L
    var a10 = 0L
    var a11 = 1L
    while (a1 != 0u) {
        val q = a0 / a1
        val ta0 = a0
        a0 = a1
        a1 = ta0 - q * a1
        val ta00 = a00
        val ta01 = a01
        a00 = a10
        a01 = a11
        a10 = ta00 - q.toLong() * a10
        a11 = ta01 - q.toLong() * a11
    }
    return ExtendedGCDResultUInt(a00, a01, a0)
}

fun UInt.modInverseOrNull(modulus: UInt): UInt? {
    require(modulus > 1u)
    var a0 = this
    var a1 = modulus
    var b0 = 1L
    var b1 = 0L
    while (a1 != 0u) {
        val q = a0 / a1
        val ta0 = a0
        a0 = a1
        a1 = ta0 - q * a1
        val tb0 = b0
        b0 = b1
        b1 = tb0 - q.toLong() * b1
    }
    return if (a0 == 1u) b0.mod(modulus.toLong()).toUInt() else null
}

fun UInt.modInverse(modulus: UInt): UInt {
    require(modulus > 1u)
    var a0 = this
    var a1 = modulus
    var b0 = 1L
    var b1 = 0L
    while (a1 != 0u) {
        val q = a0 / a1
        val ta0 = a0
        a0 = a1
        a1 = ta0 - q * a1
        val tb0 = b0
        b0 = b1
        b1 = tb0 - q.toLong() * b1
    }
    return if (a0 == 1u) b0.mod(modulus.toLong()).toUInt() else error("UInt $this has no inverse modulo $modulus")
}


fun gcd(a: ULong, b: ULong): ULong {
    var a0 = a
    var a1 = b
    while (a1 != 0uL) {
        val q = a0 / a1
        val ta0 = a0
        a0 = a1
        a1 = ta0 - q * a1
    }
    return a0
}

data class ExtendedGCDResultULong(val r: BigInteger, val s: BigInteger, val gcd: ULong)

/**
 * r*a + s*b = gcd(a,b)
 * */
fun extendedGCD(a: ULong, b: ULong): ExtendedGCDResultULong {
    var a0 = a
    var a1 = b
    var a00 = BigInteger.ONE
    var a01 = BigInteger.ZERO
    var a10 = BigInteger.ZERO
    var a11 = BigInteger.ONE
    while (a1 != 0uL) {
        val q = a0 / a1
        val ta0 = a0
        a0 = a1
        a1 = ta0 - q * a1
        val ta00 = a00
        val ta01 = a01
        a00 = a10
        a01 = a11
        a10 = ta00 - q.toBigInteger() * a10
        a11 = ta01 - q.toBigInteger() * a11
    }
    return ExtendedGCDResultULong(a00, a01, a0)
}

fun ULong.modInverseOrNull(modulus: ULong): ULong? {
    require(modulus > 1u)
    var a0 = this
    var a1 = modulus
    var b0 = BigInteger.ONE
    var b1 = BigInteger.ZERO
    while (a1 != 0uL) {
        val q = a0 / a1
        val ta0 = a0
        a0 = a1
        a1 = ta0 - q * a1
        val tb0 = b0
        b0 = b1
        b1 = tb0 - q.toBigInteger() * b1
    }
    return if (a0 == 1uL) b0.mod(modulus.toBigInteger()).ulongValue() else null
}

fun ULong.modInverse(modulus: ULong): ULong {
    require(modulus > 1u)
    var a0 = this
    var a1 = modulus
    var b0 = BigInteger.ONE
    var b1 = BigInteger.ZERO
    while (a1 != 0uL) {
        val q = a0 / a1
        val ta0 = a0
        a0 = a1
        a1 = ta0 - q * a1
        val tb0 = b0
        b0 = b1
        b1 = tb0 - q.toBigInteger() * b1
    }
    return if (a0 == 1uL) b0.mod(modulus.toBigInteger()).ulongValue() else error("ULong $this has no inverse modulo $modulus")
}

