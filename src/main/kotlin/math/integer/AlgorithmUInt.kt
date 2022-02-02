package math.integer

import math.abstract_structure.instance.RingUInt
import math.integer.modular.ModularUInt
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


suspend fun UInt.primeFactorization(): FactorizationUInt = FactorizationUInt(this, this.primeFactorizationImpl())

suspend fun UInt.primeFactorizationImpl(): List<FactorizationUIntPrimePower> {
    require(this > 0u) { "Not a positive number" }
    return when (this) {
        1u   -> emptyList()
        2u   -> listOf(FactorizationUIntPrimePower(2u, 2u, 1u))
        3u   -> listOf(FactorizationUIntPrimePower(3u, 3u, 1u))
        else -> {
            val list = mutableListOf<FactorizationUIntPrimePower>()
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
                    list += FactorizationUIntPrimePower(primePower, prime, power)
                    if (x == 1u) return list
                }
                i++
                prime = primeOf(i).toUInt()
            }
            list += FactorizationUIntPrimePower(x, x, 1u)
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
            val factorization = a.primeFactorizationImpl()
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
 * first generator of (ℤ/([prime]))^* (g^([prime]-1) = 1)
 * [prime] needs to be prime
 */
suspend fun firstMultiplicativeGeneratorOfPrimeFieldUnsafe(prime: UInt): UInt {
    return when (prime) {
        2u   -> 1u
        3u   -> 2u
        else -> {
            val a = prime - 1u
            val factorization = a.primeFactorizationImpl()
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

/**
 * first generator of (ℤ/([prime]))^* (g^([prime]-1) = 1)
 * [prime] needs to be prime, [primeMinusOne] = [prime] - 1
 */
fun firstMultiplicativeGeneratorOfPrimeFieldUnsafe(prime: UInt, primeMinusOne: FactorizationUInt): ModularUInt {
    return when (prime) {
        2u   -> ModularUInt(prime, 1u)
        3u   -> ModularUInt(prime, 2u)
        else -> {
            if (primeMinusOne.radical == primeMinusOne.value) {
                nextNumber@ for (i in 2u until prime) {
                    for (factor in primeMinusOne.factors) {
                        val exp1 = modPowerS(i, primeMinusOne.radical / factor.prime, prime)
                        if (exp1 == 1u) continue@nextNumber
                    }
                    return ModularUInt(prime, i)
                }
            } else {
                val radicalComplement = primeMinusOne.value / primeMinusOne.radical
                nextNumber@ for (i in 2u until prime) {
                    val exp = modPowerS(i, radicalComplement, prime)
                    if (exp == 1u) continue
                    for (factor in primeMinusOne.factors) {
                        val exp1 = modPowerS(exp, primeMinusOne.radical / factor.prime, prime)
                        if (exp1 == 1u) continue@nextNumber
                    }
                    return ModularUInt(prime, i)
                }
            }
            error("unknown error, make sure input $prime is prime")
        }
    }
}

fun UInt.floorLog2(): Int {
    require(this != 0u)
    return 31 - this.countLeadingZeroBits()
}

fun UInt.ceilLog2(): Int {
    require(this != 0u)
    return 32 - (this - 1u).countLeadingZeroBits()
}

/**
 * return least 2^k satisfying 2^k >= [n], k>=0
 */
fun nextTwoPower(n: UInt): FactorizationUIntPrimePower {
    if (n == 0u || n == 1u) return FactorizationUIntPrimePower(1u, 2u, 0u)
    require(n <= Int.MAX_VALUE.toUInt())
    val power = n.ceilLog2()
//    require(power != 32)
    return FactorizationUIntPrimePower(1u shl power, 2u, power.toUInt())
}