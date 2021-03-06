package math.integer.ulong

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.integer.ulong.factored.FactorizationULongPrimePower
import math.operation.product
import kotlin.math.sqrt

/**
 * Created by CowardlyLion at 2022/1/26 16:50
 */

suspend fun ULong.isPrime(): Boolean {
    return when (this) {
        0uL  -> false
        1uL  -> false
        2uL  -> true
        3uL  -> true
        else -> {
            val sqrt = sqrt(toDouble()).toULong()
            var i = 0
            var prime = primeOf(i)
            while (prime <= sqrt) {  //p*p may overflow.
                if (this % prime == 0uL) {
                    return false
                }
                i++
                prime = primeOf(i)
            }
            true
        }
    }
}

suspend fun primeFactorization1(a: ULong): List<FactorizationULongPrimePower> = a.primeFactorizationImpl()

/**
 * since primeOf(Int.MAX_VALUE)^2 - ULong.MAX_VALUE = 50685770167^2 - 2^64 - 1 = 2569047297421947207889 - 18446744073709551615 > 0,
 * it's ok to test primality of x by testing prime divisor less than sqrt(x).
 *
 * Only Two cases:
 * - one large prime factor p > sqrt(x) and other factors are small (products of other factors < sqrt(x) )
 * - all factors are small (<=sqrt(x))
 *
 * Realistically one may not calculate prime factorization of large ULong number in a reasonably short time by this method.
 * */
suspend fun ULong.primeFactorizationImpl(): List<FactorizationULongPrimePower> {
    require(this > 0uL) { "Not a positive number" }
    return when (this) {
        1uL  -> emptyList()
        2uL  -> listOf(FactorizationULongPrimePower(2uL, 1u, 2uL))
        3uL  -> listOf(FactorizationULongPrimePower(3uL, 1u, 3uL))
        else -> {
            val list = mutableListOf<FactorizationULongPrimePower>()
            var x = this
            val sqrt = sqrt(x.toDouble()).toULong()
            var i = 0
            var prime = primeOf(i)
            while (prime <= sqrt) {
                if (x.mod(prime) == 0uL) {
                    var power = 1u
                    var primePower = prime
                    x /= prime
                    while (x.mod(prime) == 0uL) {
                        power++
                        primePower *= prime
                        x /= prime
                    }
                    list += FactorizationULongPrimePower(prime, power, primePower)
                    if (x == 1uL) return list
                }
                i++
                prime = primeOf(i)
            }
            list += FactorizationULongPrimePower(x, 1u, x)
            list
        }
    }
}

suspend fun ULong.radical(): ULong {
    require(this > 0uL) { "Not a positive number" }
    return when (this) {
        1uL  -> 1uL
        2uL  -> 1uL
        3uL  -> 2uL
        else -> {
            var radical = 1uL
            var x = this
            val sqrt = sqrt(x.toDouble()).toULong()
            var i = 0
            var prime = primeOf(i)
            while (prime <= sqrt) {
                if (x.mod(prime) == 0uL) {
                    x /= prime
                    while (x.mod(prime) == 0uL) {
                        x /= prime
                    }
                    radical *= prime
                    if (x == 1uL) return radical
                }
                i++
                prime = primeOf(i)
            }
            radical *= x    //see comment at positivePrimeFactorization()
            radical
        }
    }
}

suspend fun ULong.eulerTotient(): ULong {
    require(this > 0uL) { "Not a positive number" }
    return when (this) {
        1uL  -> 1uL
        2uL  -> 1uL
        3uL  -> 2uL
        else -> {
            var eulerTotient = this
            var x = this
            val sqrt = sqrt(x.toDouble()).toULong()
            var i = 0
            var prime = primeOf(i)
            while (prime <= sqrt) {
                if (x.mod(prime) == 0uL) {
                    x /= prime
                    while (x.mod(prime) == 0uL) {
                        x /= prime
                    }
                    eulerTotient = (eulerTotient / prime) * (prime - 1uL)
                    if (x == 1uL) return eulerTotient
                }
                i++
                prime = primeOf(i)
            }
            eulerTotient = (eulerTotient / x) * (x - 1uL)     //see comment at positivePrimeFactorization()
            eulerTotient
        }
    }
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

fun isCoprime(a: ULong, b: ULong) = gcd(a, b) == 1uL

fun ULong.coprimeElements(): MutableList<ULong> {
    val list = mutableListOf<ULong>()
    for (i in 1uL until this) {
        if (isCoprime(i, this)) {
            list += i
        }
    }
    return list
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

/**
 * generator of (???/([prime]))^*
 * [prime] needs to be prime
 */
suspend fun allMultiplicativeGeneratorOfPrimeFieldUnsafe(prime: ULong): MutableList<ULong> {
    return when (prime) {
        2uL  -> mutableListOf(1uL)
        3uL  -> mutableListOf(2uL)
        else -> {
            val a = prime - 1uL
            val factorization = a.primeFactorizationImpl()
            val radical = RingULong.product(factorization.map { it.prime })
            val generators = mutableListOf<ULong>()
            if (radical == a) {
                nextNumber@ for (i in 2uL until prime) {
                    for (factor in factorization) {
                        val exp1 = modPowerS(i, radical / factor.prime, prime)
                        if (exp1 == 1uL) continue@nextNumber
                    }
                    generators += i
                }
            } else {
                val radicalComplement = a / radical
                nextNumber@ for (i in 2uL until prime) {
                    val exp = modPowerS(i, radicalComplement, prime)
                    if (exp == 1uL) continue
                    for (factor in factorization) {
                        val exp1 = modPowerS(exp, radical / factor.prime, prime)
                        if (exp1 == 1uL) continue@nextNumber
                    }
                    generators += i
                }
            }
            generators
        }
    }
}

/**
 * first generator of (???/([prime]))^*
 * [prime] needs to be prime
 */
suspend fun firstMultiplicativeGeneratorOfPrimeFieldUnsafe(prime: ULong): ULong {
    return when (prime) {
        2uL  -> 1uL
        3uL  -> 2uL
        else -> {
            val a = prime - 1uL
            val factorization = a.primeFactorizationImpl()
            val radical = RingULong.product(factorization.map { it.prime })
            if (radical == a) {
                nextNumber@ for (i in 2uL until prime) {
                    for (factor in factorization) {
                        val exp1 = modPowerS(i, radical / factor.prime, prime)
                        if (exp1 == 1uL) continue@nextNumber
                    }
                    return i
                }
            } else {
                val radicalComplement = a / radical
                nextNumber@ for (i in 2uL until prime) {
                    val exp = modPowerS(i, radicalComplement, prime)
                    if (exp == 1uL) continue
                    for (factor in factorization) {
                        val exp1 = modPowerS(exp, radical / factor.prime, prime)
                        if (exp1 == 1uL) continue@nextNumber
                    }
                    return i
                }
            }
            error("unknown error, make sure input $prime is prime")
        }
    }
}
