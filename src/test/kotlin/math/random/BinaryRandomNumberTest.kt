package math.random

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.runBlocking
import math.statistic.TaskResultStatistic
import math.statistic.TaskTimingStatistic
import math.statistic.counting
import math.timing.Task
import math.timing.TaskTimingImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import util.stdlib.toString
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/2/9 16:38
 */
internal class BinaryRandomNumberTest {

    @Test
    fun random() {
        val a = BinaryRandomNumber(Random, BigInteger.ONE)
        val b = a.toBigDecimal(10uL)
        val fraction = BigInteger.TWO.pow(10L)
        val c = b * BigDecimal.fromBigInteger(fraction)
        val c1 = c.toBigInteger()
        val remain = c - BigDecimal.fromBigInteger(c1)
        println("b: $b")
        println("c: $c")
        println("c1: $c1")
        println("c1: ${c1.toString(2)}")
        println("remain: $remain")
//        println("b: ${b.toString(2)}")    //not supported yet
    }

    @Test
    fun random1() {
        data class TwoPower(val value: BigInteger, val power: ULong)

        runBlocking {
            val tasks = TaskTimingImpl<TwoPower, BigInteger>(
                Task("               kotlin") { (value, power) -> Random.nextUInt(value.uintValue(true)).toBigInteger() },
                Task("                  FDR") { (value, power) -> Random.nextUIntFDR(value.uintValue(true)).toBigInteger() },
                Task("    binary fractional") { (value, power) ->
                    val a = BinaryRandomNumber(Random, BigInteger.ZERO)
                    val b = a.toBigDecimal(power)
                    val c = b * BigDecimal.fromBigInteger(value)
                    val c1 = c.toBigInteger()
                    val remain = c - BigDecimal.fromBigInteger(c1)
                    assertEquals(remain, BigDecimal.ZERO)
                    c1
                },
                /*Task("TPM            kotlin") { (value, power) -> TPMRandom.nextUInt(value.uintValue(true)).toBigInteger() },
                Task("TPM               FDR") { (value, power) -> TPMRandom.nextUIntFDR(value.uintValue(true)).toBigInteger() },
                Task("TPM binary fractional") { (value, power) ->
                    val a = BinaryRandomNumber(TPMRandom, BigInteger.ZERO)
                    val b = a.toBigDecimal(power)
                    val c = b * BigDecimal.fromBigInteger(value)
                    val c1 = c.toBigInteger()
                    val remain = c - BigDecimal.fromBigInteger(c1)
                    require(remain < 0.0000000000001)
                    c1
                },*/
            )
            val statistic = TaskResultStatistic(tasks)

            val digits = 11uL
            repeat(100) {
                statistic.go(TwoPower(BigInteger.TWO.pow(digits.toLong()), digits))
            }
            for (list in statistic.resultStatistic) {
                println(list.map { it.uintValue(true) }.counting().sortedBy { it.value })
            }
            println()
        }
    }

    //               kotlin: average 0.01432ms, deviation 0.12443ms
    //                  FDR: average 0.01575ms, deviation 0.31558ms
    //    binary fractional: average 0.07229ms, deviation 0.49313ms
    //TPM            kotlin: average 3.73991ms, deviation 4.84229ms
    //TPM               FDR: average 3.68963ms, deviation 0.56258ms
    //TPM binary fractional: average 3.79563ms, deviation 0.55564ms
    //total: 11.327534s
    //kotlin:   TPM is 261.2036x slower
    //   FDR:   TPM is 234.2625x slower
    //fractional:   TPM is 52.5078x slower
    @Test
    fun random2() {
        data class TwoPower(val value: BigInteger, val power: ULong)

        runBlocking {
            val tasks = TaskTimingImpl<TwoPower, BigInteger>(
                Task("               kotlin") { (value, power) -> Random.nextUInt(value.uintValue(true)).toBigInteger() },
                Task("                  FDR") { (value, power) -> Random.nextUIntFDR(value.uintValue(true)).toBigInteger() },
                Task("    binary fractional") { (value, power) ->
                    val a = BinaryRandomNumber(Random, BigInteger.ZERO)
                    val b = a.toBigDecimal(power)
                    val c = b * BigDecimal.fromBigInteger(value)
                    val c1 = c.toBigInteger()
                    val remain = c - BigDecimal.fromBigInteger(c1)
                    assertEquals(remain, BigDecimal.ZERO)
                    c1
                },
                Task("TPM            kotlin") { (value, power) -> TPMRandom.nextUInt(value.uintValue(true)).toBigInteger() },
                Task("TPM               FDR") { (value, power) -> TPMRandom.nextUIntFDR(value.uintValue(true)).toBigInteger() },
                Task("TPM binary fractional") { (value, power) ->
                    val a = BinaryRandomNumber(TPMRandom, BigInteger.ZERO)
                    val b = a.toBigDecimal(power)
                    val c = b * BigDecimal.fromBigInteger(value)
                    val c1 = c.toBigInteger()
                    val remain = c - BigDecimal.fromBigInteger(c1)
                    assertEquals(remain, BigDecimal.ZERO)
                    c1
                },
            )
            val statistic = TaskTimingStatistic(tasks)

            val digits = 10uL
            repeat(1000) {
                statistic.go(TwoPower(BigInteger.TWO.pow(digits.toLong()), digits))
            }

            statistic.printAverageAndStandardDeviation()

            val average = statistic.average()
            println("kotlin:   TPM is ${(average[3] / average[0]).toString(4u)}x slower")
            println("   FDR:   TPM is ${(average[4] / average[1]).toString(4u)}x slower")
            println("fractional:   TPM is ${(average[5] / average[2]).toString(4u)}x slower")
        }
    }

}