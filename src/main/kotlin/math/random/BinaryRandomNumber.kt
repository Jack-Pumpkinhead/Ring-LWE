package math.random

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import com.ionspin.kotlin.bignum.integer.WordArray
import util.MutableBinaryList
import util.stdlib.shl
import util.stdlib.toULong
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/2/8 20:49
 */
@OptIn(ExperimentalUnsignedTypes::class)
class BinaryRandomNumber(val random: Random, val integer: BigInteger, val fraction: MutableBinaryList = MutableBinaryList()) : Comparable<BinaryRandomNumber> {

    val randomBit = RandomBit(random)

    fun toBigDecimal(fractionDigits: ULong): BigDecimal {
        val integer1 = BigDecimal.fromBigInteger(integer)
        return if (fractionDigits == 0uL) {
            integer1
        } else {    //TODO not optimal implementation here
            ensureFractionalBits(fractionDigits)
            val dividend = BigDecimal.fromBigInteger(BigInteger.createFromWordArray(fractionWordArray63(fractionDigits), Sign.POSITIVE))
            val divisor = BigDecimal.TWO.pow(fractionDigits.toLong())
            integer1 + (dividend / divisor)
        }
    }

    /**
     * require [fractionDigits] > 0
     */
    private fun fractionWordArray63(fractionDigits: ULong): WordArray {
        val list = mutableListOf<ULong>()   //每个ULong存63位
        var uLong = 0uL
        var bits = 0u

        fun putBit(bit: Boolean) {
            uLong = (uLong shl 1u) + bit.toULong()
            bits++
            if (bits == 63u) {
                list += uLong
                bits = 0u
            }
        }

        for (i in fractionDigits - 1uL downTo 0uL) {
            putBit(fractionalBitAtUnsafe(i))
        }
        if (bits > 0u) {
            list += uLong
        }
        return list.toULongArray()
    }

    fun ensureFractionalBits(fractionDigits: ULong) {
        while (fraction.size < fractionDigits) {
            extendOneBit()
        }
    }

    fun extendOneBit() {
        fraction += randomBit.nextBit()
    }

    fun fractionalBitAt(i: ULong): Boolean {
        while (fraction.size <= i) {
            extendOneBit()
        }
        return fraction[i]
    }

    fun fractionalBitAtUnsafe(i: ULong): Boolean = fraction[i]


    override fun compareTo(other: BinaryRandomNumber): Int {
        return if (this.integer > other.integer) {
            1
        } else if (this.integer < other.integer) {
            -1
        } else {
            var i = 0uL
            while (this.fractionalBitAt(i) == other.fractionalBitAt(i)) {
                i++
            }
            if (this.fractionalBitAt(i)) 1 else -1
        }
    }



}