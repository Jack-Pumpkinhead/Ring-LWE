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
            var p = primeOf(i)
            while (p < sqrt) {  //p*p may overflow.
                if (this % p == 0L) {
                    return false
                }
                i++
                p = primeOf(i)
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

suspend fun Long.positivePrimeFactorization(): List<PrimePower> {
    if (this <= 0L) error("Not a positive number")
    return when (this) {
        1L   -> emptyList()
        2L   -> listOf(PrimePower(2L, 1))
        3L   -> listOf(PrimePower(3L, 1))
        else -> {
            val list = mutableListOf<PrimePower>()
            var x = this
            var i = 0
            var prime = primeOf(i)
            while (true) {
                if (this % prime == 0L) {
                    var power = 1
                    x /= prime
                    while (x % prime == 0L) {
                        power++
                        x /= prime
                    }
                    list += PrimePower(prime, power)
                    if (x == 1L) break
                }
                i++
                prime = primeOf(i)
            }
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
            var i = 0
            var prime = primeOf(i)
            while (true) {
                if (this % prime == 0L) {
                    x /= prime
                    while (x % prime == 0L) {
                        x /= prime
                    }
                    radical *= prime
                    if (x == 1L) break
                }
                i++
                prime = primeOf(i)
            }
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
            var i = 0
            var prime = primeOf(i)
            while (true) {
                if (this % prime == 0L) {
                    x /= prime
                    while (x % prime == 0L) {
                        x /= prime
                    }
                    eulerTotient = (eulerTotient / prime) * (prime - 1)
                    if (x == 1L) break
                }
                i++
                prime = primeOf(i)
            }
            eulerTotient
        }
    }
}