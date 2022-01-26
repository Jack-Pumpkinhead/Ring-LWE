package math.integer

import math.abstract_structure.instance.RingUInt
import math.integer.operation.modPowerS
import math.operation.product
import kotlin.math.sqrt

/**
 * Created by CowardlyLion at 2022/1/14 20:35
 */

suspend fun UInt.isPrime(): Boolean {
    return when (this) {
        0u   -> false
        1u   -> false
        2u   -> true
        3u   -> true
        else -> {
            val sqrt = sqrt(toDouble()).toUInt()
            var i = 0
            var prime = primeOf(i).toUInt()     //never overflow
            while (prime <= sqrt) {  //p*p may overflow.
                if (this % prime == 0u) {
                    return false
                }
                i++
                prime = primeOf(i).toUInt()
            }
            true
        }
    }
}

data class PrimePowerUInt(val prime: UInt, val power: UInt, val primePower: UInt) {

    fun reducePower(): PrimePowerUInt {
        require(power > 1u)
        return PrimePowerUInt(prime, power - 1u, primePower / prime)
    }

    fun eulerTotient(): UInt = when (power) {
        0u   -> 1u
        1u   -> prime - 1u
        else -> (primePower / prime) * (prime - 1u)
    }

    override fun toString(): String {
        return "$prime^$power"
    }
}


suspend fun primeFactorization1(a: UInt): List<PrimePowerUInt> = a.primeFactorization()

suspend fun UInt.primeFactorization(): List<PrimePowerUInt> {
    require(this > 0u) { "Not a positive number" }
    return when (this) {
        1u   -> emptyList()
        2u   -> listOf(PrimePowerUInt(2u, 1u, 2u))
        3u   -> listOf(PrimePowerUInt(3u, 1u, 3u))
        else -> {
            val list = mutableListOf<PrimePowerUInt>()
            var x = this
            val sqrt = sqrt(x.toDouble()).toUInt()
            var i = 0
            var prime = primeOf(i).toUInt()     //never overflow
            while (prime <= sqrt) {
                if (x.mod(prime) == 0u) {
                    var power = 1u
                    var primePower = prime
                    x /= prime
                    while (x.mod(prime) == 0u) {
                        power++
                        primePower *= prime
                        x /= prime
                    }
                    list += PrimePowerUInt(prime, power, primePower)
                    if (x == 1u) return list
                }
                i++
                prime = primeOf(i).toUInt()
            }
            list += PrimePowerUInt(x, 1u, x)
            list
        }
    }
}


suspend fun UInt.radical(): UInt {
    require(this > 0u) { "Not a positive number" }
    return when (this) {
        1u   -> 1u
        2u   -> 1u
        3u   -> 2u
        else -> {
            var radical = 1u
            var x = this
            val sqrt = sqrt(x.toDouble()).toUInt()
            var i = 0
            var prime = primeOf(i).toUInt()    //never overflow
            while (prime <= sqrt) {
                if (x.mod(prime) == 0u) {
                    x /= prime
                    while (x.mod(prime) == 0u) {
                        x /= prime
                    }
                    radical *= prime
                    if (x == 1u) return radical
                }
                i++
                prime = primeOf(i).toUInt()
            }
            radical *= x
            radical
        }
    }
}

suspend fun UInt.eulerTotient(): UInt {
    require(this > 0uL) { "Not a positive number" }
    return when (this) {
        1u   -> 1u
        2u   -> 1u
        3u   -> 2u
        else -> {
            var eulerTotient = this
            var x = this
            val sqrt = sqrt(x.toDouble()).toUInt()
            var i = 0
            var prime = primeOf(i).toUInt()    //never overflow
            while (prime <= sqrt) {
                if (x.mod(prime) == 0u) {
                    x /= prime
                    while (x.mod(prime) == 0u) {
                        x /= prime
                    }
                    eulerTotient = (eulerTotient / prime) * (prime - 1u)
                    if (x == 1u) return eulerTotient
                }
                i++
                prime = primeOf(i).toUInt()
            }
            eulerTotient = (eulerTotient / x) * (x - 1u)
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

fun isCoprime(a: UInt, b: UInt) = gcd(a, b) == 1u

fun UInt.coprimeElements(): MutableList<UInt> {
    val list = mutableListOf<UInt>()
    for (i in 1u until this) {
        if (isCoprime(i, this)) {
            list += i
        }
    }
    return list
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


/**
 * generator of (ℤ/([prime]))^*
 * [prime] needs to be prime
 */
suspend fun allMultiplicativeGeneratorOfPrimeFieldUnsafe(prime: UInt): MutableList<UInt> {
    return when (prime) {
        2u   -> mutableListOf(1u)
        3u   -> mutableListOf(2u)
        else -> {
            val a = prime - 1u
            val factorization = a.primeFactorization()
            val radical = RingUInt.product(factorization.map { it.prime })
            val generators = mutableListOf<UInt>()
            if (radical == a) {
                nextNumber@ for (i in 2u until prime) {
                    for (factor in factorization) {
                        val exp1 = modPowerS(i, radical / factor.prime, prime)
                        if (exp1 == 1u) continue@nextNumber
                    }
                    generators += i
                }
            } else {
                val radicalComplement = a / radical
                nextNumber@ for (i in 2u until prime) {
                    val exp = modPowerS(i, radicalComplement, prime)
                    if (exp == 1u) continue
                    for (factor in factorization) {
                        val exp1 = modPowerS(exp, radical / factor.prime, prime)
                        if (exp1 == 1u) continue@nextNumber
                    }
                    generators += i
                }
            }
            generators
        }
    }
}

/**
 * first generator of (ℤ/([prime]))^*
 * [prime] needs to be prime
 */
suspend fun firstMultiplicativeGeneratorOfPrimeFieldUnsafe(prime: UInt): UInt {
    return when (prime) {
        2u   -> 1u
        3u   -> 2u
        else -> {
            val a = prime - 1u
            val factorization = a.primeFactorization()
            val radical = RingUInt.product(factorization.map { it.prime })
            if (radical == a) {
                nextNumber@ for (i in 2u until prime) {
                    for (factor in factorization) {
                        val exp1 = modPowerS(i, radical / factor.prime, prime)
                        if (exp1 == 1u) continue@nextNumber
                    }
                    return i
                }
            } else {
                val radicalComplement = a / radical
                nextNumber@ for (i in 2u until prime) {
                    val exp = modPowerS(i, radicalComplement, prime)
                    if (exp == 1u) continue
                    for (factor in factorization) {
                        val exp1 = modPowerS(exp, radical / factor.prime, prime)
                        if (exp1 == 1u) continue@nextNumber
                    }
                    return i
                }
            }
            error("unknown error, make sure input $prime is prime")
        }
    }
}

