package math.random

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.runBlocking
import math.abstract_structure.algorithm.powerM
import math.abstract_structure.instance.FieldBigDecimal
import math.statistic.TaskResultStatistic
import math.statistic.TaskTimingStatistic
import math.statistic.counting
import math.timing.Task
import math.timing.TaskTimingImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import util.stdlib.repeat
import util.stdlib.toString
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/2/9 16:38
 */
internal class BinaryRandomNumberTest {

    fun fractionInteger(a: BinaryRandomNumber, digits: ULong): BigInteger {
        val decimal = a.toBigDecimal(digits)
//        println("decimal: ${decimal.toStringExpanded()}")
        val integer = BigDecimal.fromBigInteger(a.integer)
//        println("integer: ${integer1.toStringExpanded()}")
        val fraction = decimal - integer

        val fractionInteger = fraction * FieldBigDecimal.powerM(BigDecimal.TWO, digits)
        val fractionInteger1 = fractionInteger.toBigInteger()
//        println("d: ${integerD.toStringExpanded()}")
//        println("i: $integer")
        require(fractionInteger == BigDecimal.fromBigInteger(fractionInteger1))
        return fractionInteger1
    }

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

            val digits = 10uL
            repeat(10000u) {
                statistic.go(TwoPower(BigInteger.TWO.pow(digits.toLong()), digits))
            }
            for (list in statistic.resultStatistic) {
                println(list.map { it.uintValue(true) }.counting().sortedBy { it.value })
            }
            println()
        }
    }

    //               kotlin: average 0.01439ms, deviation 0.15192ms
    //                  FDR: average 0.02916ms, deviation 0.76553ms
    //    binary fractional: average 0.05289ms, deviation 0.33032ms
    //TPM            kotlin: average 3.67006ms, deviation 6.26412ms
    //TPM               FDR: average 3.48860ms, deviation 0.73966ms
    //TPM binary fractional: average 3.54263ms, deviation 0.69192ms
    //total: 10.797745800s
    //kotlin:   TPM is 254.9714x slower
    //   FDR:   TPM is 119.6283x slower
    //fractional:   TPM is 66.9748x slower
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
            repeat(1000u) {
                statistic.go(TwoPower(BigInteger.TWO.pow(digits.toLong()), digits))
            }

            statistic.printAverageAndStandardDeviation()

            val average = statistic.average()
            println("kotlin:   TPM is ${(average[3] / average[0]).toString(4u)}x slower")
            println("   FDR:   TPM is ${(average[4] / average[1]).toString(4u)}x slower")
            println("fractional:   TPM is ${(average[5] / average[2]).toString(4u)}x slower")
        }
    }

    @Test
    fun random3() {
        val r = BinaryRandomNumber(Random, BigInteger.ZERO)
        for (i in 0uL..1000uL) {
            println("i: $i, ${fractionInteger(r, i).toString(2)}")
        }
    }

}