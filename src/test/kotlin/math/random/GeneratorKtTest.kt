package math.random

import kotlinx.coroutines.runBlocking
import math.statistic.TaskResultStatistic
import math.statistic.TaskTimingStatistic
import math.statistic.counting
import math.timing.Task
import math.timing.TaskTimingImpl
import org.junit.jupiter.api.Test
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
                Task("      fast") { Random.nextUIntFDR(it) },
                Task("TPM kotlin") { TPMRandom.nextUInt(it) },
                Task("TPM   fast") { TPMRandom.nextUIntFDR(it) },
            )
            val statistic = TaskTimingStatistic(tasks)

            repeat(1000) {   //too large would run out of Java Heap space
                statistic.go(10456145u)
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    //    TPM is 700x slower
    @Test
    fun testLargeRandomUIntSpeed() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("    kotlin") { Random.nextUInt(it) },
                Task("      fast") { Random.nextUIntFDR(it) },
                Task("TPM kotlin") { TPMRandom.nextUInt(it) },
                Task("TPM   fast") { TPMRandom.nextUIntFDR(it) },
            )
            val statistic = TaskTimingStatistic(tasks)

            repeat(1000) {   //too large would run out of Java Heap space
                val num = (1u shl 31) + 1347545u
                statistic.go(num)
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    @Test
    fun testRandomUIntDistribution() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("    kotlin") { Random.nextUInt(it) },
                Task("      fast") { Random.nextUIntFDR(it) },
                Task("TPM kotlin") { TPMRandom.nextUInt(it) },
                Task("TPM   fast") { TPMRandom.nextUIntFDR(it) },
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
                Task("      fast") { (from, until) -> Random.nextUIntFDR(from, until) },
                Task("TPM kotlin") { (from, until) -> TPMRandom.nextUInt(from, until) },
                Task("TPM   fast") { (from, until) -> TPMRandom.nextUIntFDR(from, until) },
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
                Task("      fast") { range -> Random.nextUIntFDR(range) },
                Task("TPM kotlin") { range -> TPMRandom.nextUInt(range) },
                Task("TPM   fast") { range -> TPMRandom.nextUIntFDR(range) },
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
                Task("    kotlin") { Random.nextUIntFDR(1u..it) },
                Task("      fast") { runBlocking { Random.randomFactoredNumber(it).value } },
                Task("TPM kotlin") { TPMRandom.nextUIntFDR(1u..it) },
                Task("TPM   fast") { runBlocking { TPMRandom.randomFactoredNumber(it).value } },
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

//    TPM is 500x slower
    @Test
    fun testRandomFactoredNumberSpeed() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("    kotlin") { Random.nextUIntFDR(it) },
                Task("      fast") { runBlocking { Random.randomFactoredNumber(it).value } },
                Task("TPM kotlin") { TPMRandom.nextUIntFDR(it) },
                Task("TPM   fast") { runBlocking { TPMRandom.randomFactoredNumber(it).value } },
            )
            val statistic = TaskTimingStatistic(tasks)

            val r = Random.nextUInt()
            println("r: $r")
            repeat(10) {
//                println(it)
                statistic.go(r)
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

//    lazyAssert() in FactorizationUInt already checking correctness of random factored number

}