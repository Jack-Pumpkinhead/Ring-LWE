package math.integer.uint.factored

import math.integer.uint.RingUInt
import math.integer.uint.powerM
import math.integer.ulong.primeOf
import math.operation.product
import kotlin.math.sqrt

/**
 * Created by CowardlyLion at 2022/2/12 21:26
 */

fun ofPrimePower(prime: UInt, power: UInt, value: UInt = prime.powerM(power)): UIntPPI {
    require(power > 0u)
    return if (power > 1u) {
        UIntPP(value, prime, power)
    } else {
        UIntP(prime)
    }
}

fun ofPrimePowers(factors: List<UIntPPI>, value: UInt = RingUInt.product(factors.map { it.value })): UIntPPPI =
    if (factors.isEmpty()) {
        One
    } else if (factors.size == 1) {
        factors[0]
    } else {
        UIntPPP(value, factors)
    }


suspend fun UInt.primeFactorization(): UIntPPPI =
    when (this) {
        0u   -> error("cannot factored")
        1u   -> One
        else -> {
            val factors = this.primeFactorizationImpl()
            if (factors.size > 1) {
                UIntPPP(this, factors)
            } else factors[0]
        }
    }


suspend fun UInt.primeFactorizationImpl(): List<UIntPPI> {
    require(this > 1u) { "no need to factored" }
    return when (this) {
        1u   -> emptyList()
        2u   -> listOf(UIntP(2u))
        3u   -> listOf(UIntP(3u))
        else -> {
            val list = mutableListOf<UIntPPI>()
            var x = this
            val sqrt = sqrt(x.toDouble()).toUInt() + 1u  //TODO check if sqrt is correct
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
                    list += if (power == 1u) {
                        UIntP(prime)
                    } else {
                        UIntPP(primePower, prime, power)
                    }
                    if (x == 1u) return list
                }
                i++
                prime = primeOf(i).toUInt()
            }
            list += UIntP(x)
            list
        }
    }
}