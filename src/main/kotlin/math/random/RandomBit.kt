package math.random

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

    fun nextBoolean(): Boolean {
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

}