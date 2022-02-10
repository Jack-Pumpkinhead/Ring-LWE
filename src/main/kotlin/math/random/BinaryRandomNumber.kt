package math.random

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import com.ionspin.kotlin.bignum.integer.WordArray
import math.abstract_structure.algorithm.powerM
import math.abstract_structure.instance.FieldBigDecimal
import math.half
import util.MutableBinaryList
import util.stdlib.shl
import util.stdlib.toULong
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/2/8 20:49
 *
 * represent a random number with value sign * ([integer] + [fraction])
 */
@OptIn(ExperimentalUnsignedTypes::class)
class BinaryRandomNumber(val random: Random, val isPositive: Boolean = true, val integer: BigInteger = BigInteger.ZERO, val fraction: MutableBinaryList = MutableBinaryList()) : Comparable<BinaryRandomNumber> {

    init {
        require(!integer.isNegative)
    }

    val randomBit = RandomBit(random)

    fun absoluteValueBigDecimal(fractionDigits: ULong): BigDecimal {
        val integer1 = BigDecimal.fromBigInteger(integer)
        return if (fractionDigits == 0uL) {
            integer1
        } else {    //TODO not optimal implementation here
            ensureFractionalBits(fractionDigits)
            val bigInteger = BigInteger.createFromWordArray(fractionWordArray63(fractionDigits), Sign.POSITIVE)
            val dividend = BigDecimal.fromBigInteger(bigInteger)
            val divisor = FieldBigDecimal.powerM(half, fractionDigits)
            integer1 + (dividend * divisor)  //Binary digits = Decimal digits   //don't use division
        }
    }

    fun toBigDecimal(fractionDigits: ULong): BigDecimal =
        if (isPositive) {
            absoluteValueBigDecimal(fractionDigits)
        } else {
            absoluteValueBigDecimal(fractionDigits).negate()
        }

    fun toDouble(): Double {
        TODO()
//        Double.fromBits()
    }

    /**
     * require [fractionDigits] > 0
     */
    private fun fractionWordArray63(fractionDigits: ULong): WordArray {
        val list = mutableListOf<ULong>()   //每个ULong存63位

        val words = fractionDigits / 63uL
        val remaining = fractionDigits.mod(63uL)
        for (w in 1uL..words) {
            val start = fractionDigits - (w * 63uL)
            var uLong = 0uL
            for (i in 0uL until 63uL) {
                uLong = (uLong shl 1u) + fractionalBitAtUnsafe(start + i).toULong()
            }
            list += uLong
        }
        var uLong = 0uL
        for (i in 0uL until remaining) {
            uLong = (uLong shl 1u) + fractionalBitAtUnsafe(i).toULong()
        }
        list += uLong
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


    override fun compareTo(other: BinaryRandomNumber): Int =
        if (this.isPositive) {
            if (other.isPositive) compareToPositivePart(other) else 1
        } else {
            if (other.isPositive) -1 else -compareToPositivePart(other)
        }

    fun compareToPositivePart(other: BinaryRandomNumber): Int = when {
        this.integer == other.integer -> {
            var i = 0uL
            while (this.fractionalBitAt(i) == other.fractionalBitAt(i)) {
                i++
            }
            if (this.fractionalBitAt(i)) 1 else -1
        }
        this.integer > other.integer  -> 1
        else                          -> -1
    }

    /**
     * return if this < 1/2
     */
    fun lessThanHalf(): Boolean =
        if (isPositive) {
            if (integer == BigInteger.ZERO) {
                !fractionalBitAt(0uL)
            } else false
        } else true
}