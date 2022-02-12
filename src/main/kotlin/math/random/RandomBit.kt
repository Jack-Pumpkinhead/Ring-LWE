package math.random

import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/2/9 15:03
 */
class RandomBit(val random: Random, var randomUInt: UInt = 0u, var bitsRemaining: UInt = 0u) {

    fun nextBitUInt(): UInt {
        return if (bitsRemaining != 0u) {
            val result = randomUInt.and(1u)
            randomUInt = randomUInt shr 1
            bitsRemaining--
            result
        } else {
            randomUInt = random.nextUInt()
            val result = randomUInt.and(1u)
            randomUInt = randomUInt shr 1
            bitsRemaining = 31u
            result
        }
    }

    fun nextBit(): Boolean {
        return if (bitsRemaining != 0u) {
            val result = randomUInt.and(1u)
            randomUInt = randomUInt shr 1
            bitsRemaining--
            result == 1u
        } else {
            randomUInt = random.nextUInt()
            val result = randomUInt.and(1u)
            randomUInt = randomUInt shr 1
            bitsRemaining = 31u
            result == 1u
        }
    }

    fun nextBigInteger(bits: ULong): BigInteger {
        var result = BigInteger.ZERO
        for (i in 0uL until bits) {
            result = result.shl(1) + if (nextBit()) BigInteger.ONE else BigInteger.ZERO
        }
        return result
    }

}