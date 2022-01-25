package util

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.bitAt
import math.setBitAt

/**
 * Created by CowardlyLion at 2022/1/16 21:25
 */
class InfiniteBitSet {

    /**
     * ONLY capable of bits(index) < Int.MAX_VALUE * 64
     * TODO use ULongArray
     * */
    val base = mutableListOf<ULong>()

    fun bitAt(i: BigInteger): Boolean {
        require(i.isNegative.not())

        val wordIndex = i / num64
        return if (wordIndex >= base.size.toBigInteger()) false else {
            base[wordIndex.intValue()].bitAt(i.mod(num64).uintValue())
        }
    }

    fun bitAt(i: UInt): Boolean {
        val wordIndex = i / 64u
        return if (wordIndex >= base.size.toUInt()) false else {
            base[wordIndex.toInt()].bitAt(i.mod(64u))
        }
    }


    fun setBitAt(i: BigInteger, bit: Boolean) {
        require(i.isNegative.not())
        val (quotient, remainder) = i.divideAndRemainder(num64)
        val wordIndex = quotient.intValue(true)

        if (bit) {
            while (wordIndex >= base.size) {
                base += 0uL
            }
            base[wordIndex].setBitAt(remainder.uintValue(), true)
        } else if (wordIndex < base.size) {
            base[wordIndex].setBitAt(remainder.uintValue(), false)
        }
    }

    fun setBitAt(i: UInt, bit: Boolean) {
        val wordIndex = (i / 64u).toInt()
        val remainder = i.mod(64u)

        if (bit) {
            while (wordIndex >= base.size) {
                base += 0uL
            }
            base[wordIndex].setBitAt(remainder, true)
        } else if (wordIndex < base.size) {
            base[wordIndex].setBitAt(remainder, false)
        }
    }


}

val num64 = BigInteger(64)
