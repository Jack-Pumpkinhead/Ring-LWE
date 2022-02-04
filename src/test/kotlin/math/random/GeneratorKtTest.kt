package math.random

import kotlinx.coroutines.runBlocking
import math.statistic.TaskResultStatistic
import math.statistic.TaskTimingStatistic
import math.statistic.counting
import math.timing.Task
import math.timing.TaskTimingImpl
import org.junit.jupiter.api.Test
import util.stdlib.toString
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/2/2 18:29
 */
internal class GeneratorKtTest {

    @Test
    fun testRandomUIntSpeed() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("    kotlin") { Random.nextUInt(it) },
                Task("       FDR") { Random.nextUIntFDR(it) },
                Task("TPM kotlin") { TPMRandom.nextUInt(it) },
                Task("TPM    FDR") { TPMRandom.nextUIntFDR(it) },
            )
            val statistic = TaskTimingStatistic(tasks)

            repeat(1000) {   //too large would run out of Java Heap space
                statistic.go(10456145u)
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    //kotlin:   TPM is 645.4143x slower
    //   FDR:   TPM is 406.5634x slower
    @Test
    fun testLargeRandomUIntSpeed() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("    kotlin") { Random.nextUInt(it) },
                Task("       FDR") { Random.nextUIntFDR(it) },
                Task("TPM kotlin") { TPMRandom.nextUInt(it) },
                Task("TPM    FDR") { TPMRandom.nextUIntFDR(it) },
            )
            val statistic = TaskTimingStatistic(tasks)

            repeat(1000) {   //too large would run out of Java Heap space since statistic storing data.
                val num = (1u shl 31) + 1347545u
                statistic.go(num)
            }
            statistic.printAverageAndStandardDeviation()

            val average = statistic.average()
            println("kotlin:   TPM is ${(average[2] / average[0]).toString(4u)}x slower")
            println("   FDR:   TPM is ${(average[3] / average[1]).toString(4u)}x slower")
        }
    }

    @Test
    fun testRandomUIntDistribution() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("    kotlin") { Random.nextUInt(it) },
                Task("       FDR") { Random.nextUIntFDR(it) },
                Task("TPM kotlin") { TPMRandom.nextUInt(it) },
                Task("TPM    FDR") { TPMRandom.nextUIntFDR(it) },
            )
            val statistic = TaskResultStatistic(tasks)

            repeat(1000) {
                statistic.go(105u)
            }
            for (list in statistic.resultStatistic) {
                println(list.counting().sortedBy { it.value })
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
                println(list.counting().sortedBy { it.value })
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
                println(list.counting().sortedBy { it.value })
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
                println(list.counting().sortedBy { it.value })
            }
            println()
        }
    }

    //            FDR:   TPM is 24.1077x slower
    //factored kotlin:   TPM is 423.4949x slower
    //factored    FDR:   TPM is 819.9691x slower

    //            FDR:   TPM is 20.0348x slower
    //factored kotlin:   TPM is 503.5489x slower
    //factored    FDR:   TPM is 619.6078x slower
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

    //            FDR:   TPM is 168.4941x slower
    //factored kotlin:   TPM is 283.0870x slower
    //factored    FDR:   TPM is 344.5506x slower
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