package math

import math.cache.primeOf
import kotlin.math.absoluteValue
import kotlin.math.sqrt

/**
 * Created by CowardlyLion at 2022/1/5 19:25
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
    if (this <= 0L) error("Not a positive number")
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
    if (this <= 0L) error("Not a positive number")
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
    if (this <= 0L) error("Not a positive number")
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