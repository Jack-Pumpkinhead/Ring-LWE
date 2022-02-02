package math.integer

import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.runBlocking
import util.stdlib.lazyAssert2


/**
 * Created by CowardlyLion at 2022/1/27 11:41
 *
 * [value] = [prime]^[power]
 */
data class FactorizationUIntPrimePower(val value: UInt, val prime: UInt, val power: UInt) {

    init {
        lazyAssert2 {
            runBlocking {
                assert(prime.isPrime())
            }
            assert(value.toBigInteger() == prime.toBigInteger().pow(power.toLong()))
        }
    }

    fun toPrime(): FactorizationUIntPrime {
        require(power == 1u)
        return FactorizationUIntPrime(prime)
    }

    fun reducePowerByOne(): FactorizationUIntPrimePower {
        require(power > 1u)
        return FactorizationUIntPrimePower(value / prime, prime, power - 1u)
    }

    val eulerTotient: UInt by lazy {
        when (power) {
            0u   -> 1u
            1u   -> prime - 1u
            else -> (value / prime) * (prime - 1u)
        }
    }

    val radical: UInt get() = prime

    override fun toString(): String {
        return "$prime^$power"
    }
}