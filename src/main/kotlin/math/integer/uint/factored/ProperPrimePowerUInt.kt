package math.integer.uint.factored

import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.runBlocking
import math.integer.uint.isPrime
import util.stdlib.lazyAssert2


/**
 * Created by CowardlyLion at 2022/1/27 11:41
 *
 * [value] = [prime]^[power], [power] > 1
 *
 * represent a proper power of prime with power > 1
 */
class ProperPrimePowerUInt(override val value: UInt, override val prime: UInt, override val power: UInt) : UIntPPI {

    init {
        lazyAssert2 {
            assert(power > 1u)
            runBlocking {
                assert(prime.isPrime())
            }
            assert(value.toBigInteger() == prime.toBigInteger().pow(power.toLong()))
        }
    }

    fun reducePowerByOne(): UIntPPI {
        require(power > 1u)
        return if (power == 2u) {
            PrimeUInt(prime)
        } else {
            ProperPrimePowerUInt(value / prime, prime, power - 1u)
        }
    }

    /**
     * require [power] > 2u
     */
    fun reducePowerByOneUnsafe(): ProperPrimePowerUInt = ProperPrimePowerUInt(value / prime, prime, power - 1u)

    /**
     * require [power] = 2u
     */
    fun reducePowerByOneToPrimeUnsafe(): PrimeUInt = PrimeUInt(prime)

    override val eulerTotient: UInt by lazy {
        (value / prime) * (prime - 1u)
    }

    override fun toString(): String {
        return "$prime^$power"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UIntPPPI) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}