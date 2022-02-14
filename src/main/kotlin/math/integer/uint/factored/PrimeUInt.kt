package math.integer.uint.factored

import kotlinx.coroutines.runBlocking
import math.integer.uint.isPrime
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/27 12:03
 *
 * represent a prime UInt number
 */
class PrimeUInt(override val value: UInt) : UIntPI {

    init {
        lazyAssert2 {
            runBlocking {
                assert(value.isPrime())
            }
        }
    }

    override fun toString(): String = value.toString()

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