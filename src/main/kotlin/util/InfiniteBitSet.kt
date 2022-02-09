package util

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import util.bit.bitAt
import math.num64
import util.bit.copyWithBitChange

/**
 * Created by CowardlyLion at 2022/1/16 21:25
 */
class InfiniteBitSet {

    /**
     * ONLY capable of bits < Int.MAX_VALUE * 64
     * TODO use MutableList<ULongArray>
     * */
    val base = mutableListOf<ULong>()

    fun bitAt(i: BigInteger): Boolean {
        require(i.isNegative.not())

        val wordIndex = i / num64
        return if (wordIndex < base.size.toBigInteger()) {
            base[wordIndex.intValue()].bitAt(i.mod(num64).uintValue())
        } else false
    }

    fun bitAt(i: UInt): Boolean {
        val wordIndex = i / 64u
        return if (wordIndex < base.size.toUInt()) {
            base[wordIndex.toInt()].bitAt(i.mod(64u))
        } else false
    }


    fun setBitAt(i: BigInteger, bit: Boolean) {
        require(i.isNegative.not())
        val (quotient, remainder) = i.divideAndRemainder(num64)
        val wordIndex = quotient.intValue(true)

        if (bit) {
            while (wordIndex >= base.size) {
                base += 0uL
            }
            base[wordIndex] = base[wordIndex].copyWithBitChange(remainder.uintValue(), true)
        } else if (wordIndex < base.size) {
            base[wordIndex] = base[wordIndex].copyWithBitChange(remainder.uintValue(), false)
        }
    }

    fun setBitAt(i: UInt, bit: Boolean) {
        val wordIndex = (i / 64u).toInt()

        if (bit) {
            while (wordIndex >= base.size) {
                base += 0uL
            }
            base[wordIndex] = base[wordIndex].copyWithBitChange(i.mod(64u), true)
        } else if (wordIndex < base.size) {
            base[wordIndex] = base[wordIndex].copyWithBitChange(i.mod(64u), false)
        }
    }

}

