package math.random

import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.runBlocking
import math.statistic.TaskResultStatistic
import math.statistic.TaskTimingStatistic
import math.statistic.countingUInt
import math.timing.Task
import math.timing.TaskTimingImpl
import org.junit.jupiter.api.Test
import util.stdlib.toString
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/2/2 18:29
 */
internal class UniformRandomHelperKtTest {

    //    kotlin: average 0.00961ms, deviation 0.06764ms
    //       FDR: average 0.03158ms, deviation 0.83412ms
    //    bigFDR: average 0.02284ms, deviation 0.12369ms
    //TPM kotlin: average 3.70544ms, deviation 5.01683ms
    //TPM    FDR: average 3.57737ms, deviation 0.61637ms
    //TPM bigFDR: average 3.58288ms, deviation 0.62096ms
    //total: 10.929726700s
    //kotlin:   TPM is 385.4214x slower
    //   FDR:   TPM is 113.2904x slower
    //bigFDR:   TPM is 156.8549x slower
    @Test
    fun testRandomUIntSpeed() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("    kotlin") { Random.nextUInt(it) },
                Task("       FDR") { Random.nextUIntFDR(it) },
                Task("    bigFDR") { Random.nextBigIntegerFDR(it.toBigInteger()).uintValue(true) },
                Task("TPM kotlin") { TPMRandom.nextUInt(it) },
                Task("TPM    FDR") { TPMRandom.nextUIntFDR(it) },
                Task("TPM bigFDR") { TPMRandom.nextBigIntegerFDR(it.toBigInteger()).uintValue(true) },
            )
            val statistic = TaskTimingStatistic(tasks)

            repeat(1000) {   //too large would run out of Java Heap space
                statistic.go(10456145u)
            }

            statistic.printAverageAndStandardDeviation()

            val average = statistic.average()
            println("kotlin:   TPM is ${(average[3] / average[0]).toString(4u)}x slower")
            println("   FDR:   TPM is ${(average[4] / average[1]).toString(4u)}x slower")
            println("bigFDR:   TPM is ${(average[5] / average[2]).toString(4u)}x slower")
        }
    }

    //    kotlin: average 0.01021ms, deviation 0.07652ms
    //       FDR: average 0.03311ms, deviation 0.88417ms
    //    bigFDR: average 0.02326ms, deviation 0.07889ms
    //TPM kotlin: average 7.49704ms, deviation 6.71323ms
    //TPM    FDR: average 5.22666ms, deviation 1.97148ms
    //TPM bigFDR: average 5.39404ms, deviation 1.94152ms
    //total: 18.184314300s
    //kotlin:   TPM is 734.1401x slower
    //   FDR:   TPM is 157.8716x slower
    //bigFDR:   TPM is 231.9018x slower
    @Test
    fun testLargeRandomUIntSpeed() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("    kotlin") { Random.nextUInt(it) },
                Task("       FDR") { Random.nextUIntFDR(it) },
                Task("    bigFDR") { Random.nextBigIntegerFDR(it.toBigInteger()).uintValue(true) },
                Task("TPM kotlin") { TPMRandom.nextUInt(it) },
                Task("TPM    FDR") { TPMRandom.nextUIntFDR(it) },
                Task("TPM bigFDR") { TPMRandom.nextBigIntegerFDR(it.toBigInteger()).uintValue(true) },
            )
            val statistic = TaskTimingStatistic(tasks)

            repeat(1000) {   //too large would run out of Java Heap space since statistic storing data.
                val num = (1u shl 31) + 1347545u
                statistic.go(num)
            }
            statistic.printAverageAndStandardDeviation()

            val average = statistic.average()
            println("kotlin:   TPM is ${(average[3] / average[0]).toString(4u)}x slower")
            println("   FDR:   TPM is ${(average[4] / average[1]).toString(4u)}x slower")
            println("bigFDR:   TPM is ${(average[5] / average[2]).toString(4u)}x slower")
        }
    }

    @Test
    fun testRandomUIntDistribution() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("    kotlin") { Random.nextUInt(it) },
                Task("       FDR") { Random.nextUIntFDR(it) },
                Task("    bigFDR") { Random.nextBigIntegerFDR(it.toBigInteger()).uintValue(true) },
                Task("TPM kotlin") { TPMRandom.nextUInt(it) },
                Task("TPM    FDR") { TPMRandom.nextUIntFDR(it) },
                Task("TPM bigFDR") { TPMRandom.nextBigIntegerFDR(it.toBigInteger()).uintValue(true) },
            )
            val statistic = TaskResultStatistic(tasks)

            repeat(1000) {
                statistic.go(105u)
            }
            for (list in statistic.resultStatistic) {
                println(list.countingUInt().sortedBy { it.value })
            }
            println()
        }
        runBlocking {
            val tasks = TaskTimingImpl<Pair<UInt, UInt>, UInt>(
                Task("    kotlin") { (from, until) -> Random.nextUInt(from, until) },
                Task("       FDR") { (from, until) -> Random.nextUIntFDR(from, until) },
                Task("TPM kotlin") { (from, until) -> TPMRandom.nextUInt(from, until) },
                Task("TPM    FDR") { (from, until) -> TPMRandom.nextUIntFDR(from, until) },
            )
            val statistic = TaskResultStatistic(tasks)

            repeat(1000) {
                statistic.go(23u to 105u)
            }
            for (list in statistic.resultStatistic) {
                println(list.countingUInt().sortedBy { it.value })
            }
            println()
        }

        runBlocking {
            val tasks = TaskTimingImpl<UIntRange, UInt>(
                Task("    kotlin") { range -> Random.nextUInt(range) },
                Task("       FDR") { range -> Random.nextUIntFDR(range) },
                Task("TPM kotlin") { range -> TPMRandom.nextUInt(range) },
                Task("TPM    FDR") { range -> TPMRandom.nextUIntFDR(range) },
            )
            val statistic = TaskResultStatistic(tasks)

            repeat(1000) {
                statistic.go(23u..105u)
            }
            for (list in statistic.resultStatistic) {
                println(list.countingUInt().sortedBy { it.value })
            }
            println()
        }
    }

    @Test
    fun testDistribution() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("    kotlin") { Random.nextUInt(it) },
                Task("       FDR") { Random.nextUIntFDR(it) },
                Task("    bigFDR") { Random.nextBigIntegerFDR(it.toBigInteger()).uintValue(true) },
            )
            val statistic = TaskResultStatistic(tasks)

            repeat(100000) {
                statistic.go(105u)
            }
            for (list in statistic.resultStatistic) {
                println(list.countingUInt().sortedBy { it.value })
            }
            println()
        }
    }

    @Test
    fun testRandomFactoredNumberDistribution() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("                FDR") { Random.nextUIntFDR(1u..it) },
                Task("    factored kotlin") { runBlocking { Random.randomFactoredNumber(it).value } },
                Task("    factored    FDR") { runBlocking { Random.randomFactoredNumberFDR(it).value } },
                Task("TPM             FDR") { TPMRandom.nextUIntFDR(1u..it) },
                Task("TPM factored kotlin") { runBlocking { TPMRandom.randomFactoredNumber(it).value } },
                Task("TPM factored    FDR") { runBlocking { TPMRandom.randomFactoredNumberFDR(it).value } },
            )
            val statistic = TaskResultStatistic(tasks)

            repeat(200) {
                statistic.go(45u)
            }
            for (list in statistic.resultStatistic) {
                println(list.countingUInt().sortedBy { it.value })
            }
            println()
        }
    }

    //r: 4253509286
    //                FDR: average 1.98715ms, deviation 6.23237ms
    //    factored kotlin: average 5.48565ms, deviation 4.73894ms
    //    factored    FDR: average 2.59341ms, deviation 1.46676ms
    //TPM             FDR: average 18.65238ms, deviation 48.96807ms
    //TPM factored kotlin: average 2043.71689ms, deviation 1885.34117ms
    //TPM factored    FDR: average 3684.78914ms, deviation 3407.11605ms
    //total: 57.572246200s
    //            FDR:   TPM is 9.3865x slower
    //factored kotlin:   TPM is 372.5569x slower
    //factored    FDR:   TPM is 1420.8278x slower
    @Test
    fun testRandomFactoredNumberSpeed() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("                FDR") { Random.nextUIntFDR(1u..it) },
                Task("    factored kotlin") { runBlocking { Random.randomFactoredNumber(it).value } },
                Task("    factored    FDR") { runBlocking { Random.randomFactoredNumberFDR(it).value } },
                Task("TPM             FDR") { TPMRandom.nextUIntFDR(1u..it) },
                Task("TPM factored kotlin") { runBlocking { TPMRandom.randomFactoredNumber(it).value } },
                Task("TPM factored    FDR") { runBlocking { TPMRandom.randomFactoredNumberFDR(it).value } },
            )
            val statistic = TaskTimingStatistic(tasks)

//            val r = Random.nextUInt(1u..UInt.MAX_VALUE)   //IDEA: this range is empty???
            val r = Random.nextUInt()
            println("r: $r")
            if (r != 0u) {
                repeat(10) {
//                println(it)
                    statistic.go(r)
                }
                statistic.printAverageAndStandardDeviation()
                val average = statistic.average()
                println("            FDR:   TPM is ${(average[3] / average[0]).toString(4u)}x slower")
                println("factored kotlin:   TPM is ${(average[4] / average[1]).toString(4u)}x slower")
                println("factored    FDR:   TPM is ${(average[5] / average[2]).toString(4u)}x slower")
            }
        }
    }

    //                FDR: average 0.18613ms, deviation 1.77065ms
    //    factored kotlin: average 0.31443ms, deviation 0.69299ms
    //    factored    FDR: average 0.27444ms, deviation 0.33950ms
    //TPM             FDR: average 4.42460ms, deviation 15.36563ms
    //TPM factored kotlin: average 105.36206ms, deviation 112.08160ms
    //TPM factored    FDR: average 133.63499ms, deviation 119.14794ms
    //total: 24.419664400s
    //            FDR:   TPM is 23.7721x slower
    //factored kotlin:   TPM is 335.0912x slower
    //factored    FDR:   TPM is 486.9406x slower
    @Test
    fun testSmallRandomFactoredNumberSpeed() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("                FDR") { Random.nextUIntFDR(1u..it) },
                Task("    factored kotlin") { runBlocking { Random.randomFactoredNumber(it).value } },
                Task("    factored    FDR") { runBlocking { Random.randomFactoredNumberFDR(it).value } },
                Task("TPM             FDR") { TPMRandom.nextUIntFDR(1u..it) },
                Task("TPM factored kotlin") { runBlocking { TPMRandom.randomFactoredNumber(it).value } },
                Task("TPM factored    FDR") { runBlocking { TPMRandom.randomFactoredNumberFDR(it).value } },
            )
            val statistic = TaskTimingStatistic(tasks)

            repeat(100) {
                val r = Random.nextUInt(1u..100u)
//                println(it)
                statistic.go(r)
            }
            statistic.printAverageAndStandardDeviation()
            val average = statistic.average()
            println("            FDR:   TPM is ${(average[3] / average[0]).toString(4u)}x slower")
            println("factored kotlin:   TPM is ${(average[4] / average[1]).toString(4u)}x slower")
            println("factored    FDR:   TPM is ${(average[5] / average[2]).toString(4u)}x slower")
        }
    }

//    lazyAssert() in FactorizationUInt already checking correctness of random factored number

}