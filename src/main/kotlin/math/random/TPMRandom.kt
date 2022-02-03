package math.random

import tss.TpmFactory
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/2/2 23:57
 */
object TPMRandom : Random() {

    val tpm = TpmFactory.platformTpm()

    override fun nextBits(bitCount: Int): Int {
        if (bitCount == 0) return 0
        if (bitCount.mod(8) == 0) {
            val bytes = bitCount / 8   //8, 16, 24, 32
            val randomBytes = tpm.GetRandom(bytes)!!

            var a = randomBytes[0].toUByte().toUInt()
            for (i in 1 until bytes) {
                a = a shl 8
                a += randomBytes[i].toUByte().toUInt()
            }
            return a.toInt()
        } else {
            val bytes = (bitCount / 8) + 1    //1..7, 9..15, 17..23, 25..31
            val randomBytes = tpm.GetRandom(bytes)!!

            var a = 0u
            for (i in 0 until bytes - 1) {
                a = a shl 8
                a += randomBytes[i].toUByte().toUInt()
            }
            val remainingBit = bitCount.mod(8)
            a = a shl remainingBit
            a += randomBytes.last().toUByte().mod(1u shl remainingBit)
            return a.toInt()
        }
    }

    /**
     * Gets the next random [bitCount] number of bits.
     *
     * Generates an `UInt` whose lower [bitCount] bits are filled with random values and the remaining upper bits are zero.
     *
     * [bitCount] must be in range 0..32
     */
    fun nextBits(bitCount: UInt): UInt {
        require(bitCount <= 32u)
        return nextBits(bitCount.toInt()).toUInt()
    }

}