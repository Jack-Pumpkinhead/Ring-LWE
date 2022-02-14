package test

import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode.*
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import math.integer.uint.factored.twoPower
import math.random.RandomBit
import math.statistic.TaskTimingStatistic
import math.timing.EqualTaskTimingImpl
import math.timing.Task
import org.junit.jupiter.api.Test
import tss.TpmFactory
import util.stdlib.repeat
import util.stdlib.shl
import util.toBigDecimal
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/5 17:23
 */
internal class Test {

    @Test
    fun testBigInteger() {
        for (i in 0..1234) {
            val x = BigInteger(i)
            println("$i.numberOfDecimalDigits() = ${x.numberOfDecimalDigits()}")
        }
    }


    @Test
    fun testTPM() {
        val tpm = TpmFactory.platformTpm()
        val randomBytes = tpm.GetRandom(10)!!
        println(randomBytes.joinToString())
    }

    @Test
    fun launch() {
        runBlocking {
            //launch don't freeze captured variable
            /*var i = 0u
             launch {
                 delay(5L)
                 println(i)
             }
             delay(5L)
             i++*/

            for (i in 0u..10u) {
                launch {
                    delay(10 - i.toLong())  //10,9,8,7,6,5,4,3,2,0,1
                    println(i)
                }
            }

            //[i] is val, so would not change
            for (i in 0u..10u) {
                launch {
                    delay((10 - i.toLong()) * 1000)  //10,9,8,7,6,5,4,3,2,0,1
                    println(i)
                }
            }

        }
    }

    @Test
    fun bigDecimal() {
        val a = 123L.toBigDecimal(0L, null)
//        "aaa".toBigDecimal()
        println(a)
    }

    @Test
    fun bigDecimalDivision() {
        val a = 1637603.toBigDecimal(0L, null)
        val b = 2097152.toBigDecimal(0L, null)
        println(a)
        println(b)
//        val div = a.divide(b, DecimalMode.DEFAULT)
        val div = a.divide(b, DecimalMode.US_CURRENCY)
        println(div)
    }

    @Test
    fun bigDecimalDivision1() {
        val a = 1637603.toBigDecimal(0L, null)
        val b = 2097152.toBigDecimal(0L, null)
        println(a.toStringExpanded())
        println(b.toStringExpanded())
        val div = a.divide(b, DecimalMode(21))
        println(div.toStringExpanded())
    }

    @Test
    fun bigDecimalDivisionJava() {
//        val a = BigDecimal(1637603)
//        val b = BigDecimal(2097152)
        val a = BigDecimal.valueOf(1637603L)
        val b = BigDecimal.valueOf(2097152L)
        println(a)
        println(b)
        val div = a.divide(b, 25, RoundingMode.UNNECESSARY)
        println(div)
    }

    @Test
    fun bigDecimalDivisionJava1() {
//        val a = BigDecimal(1637603)
//        val b = BigDecimal(2097152)
        val a = BigDecimal.valueOf(1637603L)
        val b = BigDecimal.valueOf(2097152L)
        println(a)
        println(b)
        val div = a / b
        println(div)
    }

    @Test
    fun shl() {
        val zero = BigInteger.ZERO
        println(zero.shl(1))
        println("sign: ${zero.getSign()}")

        val zero1 = BigInteger.ONE - 1uL.toBigInteger()
        println("${zero1.shl(1)}")
        println("sign: ${zero1.getSign()}")
    }

    @Test
    fun roundToBigInteger() {
        val random = RandomBit(Random)
        for (i in 1u..1000u) {
            var randomDivision = random.nextBigInteger(1000uL).toBigDecimal().divide((random.nextBigInteger(1000uL) + BigInteger.ONE).toBigDecimal(), DecimalMode(5, ROUND_HALF_TO_EVEN))
            if (random.nextBit()) {
                randomDivision = -randomDivision
            }
            val round = randomDivision.roundToDigitPositionAfterDecimalPoint(0L, CEILING).toBigInteger()
            println(" ${randomDivision.toStringExpanded()} round to $round")
        }
    }

    @Test
    fun testCachedGetSpeed() {
        runBlocking {
            val tasks = EqualTaskTimingImpl<UInt, UInt>(
                Task("cachedGet") { twoPower(it) },
                Task("compute") { 1u.shl(it) }
            )
            val statistic = TaskTimingStatistic(tasks)

            repeat(10000u) {
                statistic.go(Random.nextUInt(32u))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

}