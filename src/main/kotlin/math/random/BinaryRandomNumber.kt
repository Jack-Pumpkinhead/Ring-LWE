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
            val bigInteger = BigInteger.createFromWordArray(fractionWordArray63(fractionDigits), Sign.POSITIVE)
//            println()

//            bigInteger.shl(1)
            val dividend = BigDecimal.fromBigInteger(bigInteger)
//            println("dividend: $bigInteger, ${bigInteger.toString(2)}")
//            println("fraction: ${fraction.list.joinToString { it.toString(2).reversed() }}")
//            println("fraction: ${fraction.list.joinToString { it.toString(2).reversed() }}")
//            println("fractionDigits: $fractionDigits")
//            val divisor = BigDecimal.fromBigInteger(BigInteger.TWO.pow(fractionDigits.toLong()), DecimalMode(fractionDigits.toLong()))  //precision of divisor also need to be specified.
//            val divisor = BigDecimal.fromBigInteger(BigInteger.TWO.pow(fractionDigits.toLong()))
            val divisor = FieldBigDecimal.powerM(half, fractionDigits)
//            val divisor1 = BigInteger.TWO.pow(fractionDigits.toLong())
//            println("divisor: ${divisor.toStringExpanded()}")
//            println("divisor1: $divisor1")
            integer1 + (dividend * divisor)  //Binary digits = Decimal digits   //don't use division
        }
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


/*private fun fractionBigInteger(fractionDigits: ULong): BigInteger {
    var integer = BigInteger.ZERO
    var uLong = 0uL
    var bits = 0u

    fun putBit(bit: Boolean) {
        uLong = (uLong shl 1u) + bit.toULong()
        bits++
        if (bits == 63u) {
            integer = integer.shl(63) + uLong.toBigInteger()
            uLong = 0uL //need to reset uLong
            bits = 0u
        }
    }

//        for (i in 0uL until fractionDigits) {
    for (i in fractionDigits - 1uL downTo 0uL) {
        putBit(fractionalBitAtUnsafe(i))
    }
    if (bits > 0u) {
        list += uLong
    }
    return list.toULongArray()
}*/

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


    override fun compareTo(other: BinaryRandomNumber): Int = when {
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


}