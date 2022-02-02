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
                Task("kotlin random UInt") { Random.nextUInt(it) },
                Task("fast random UInt") { Random.nextUIntFDR(it) }
            )
            val statistic = TaskTimingStatistic(tasks)

            repeat(1000000) {   //too large would run out of Java Heap space
                statistic.go(1045619845u)
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    @Test
    fun testLargeRandomUIntSpeed() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("kotlin random UInt") { Random.nextUInt(it) },
                Task("fast random UInt") { Random.nextUIntFDR(it) }
            )
            val statistic = TaskTimingStatistic(tasks)

            repeat(1000000) {   //too large would run out of Java Heap space
                val num = (1u shl 31) + 13445u
                statistic.go(num)
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    @Test
    fun testRandomUIntDistribution() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("kotlin random UInt") { Random.nextUInt(it) },
                Task("fast random UInt") { Random.nextUIntFDR(it) }
            )
            val statistic = TaskResultStatistic(tasks)

            repeat(1000000) {
                statistic.go(105u)
            }
            val list0 = statistic.resultStatistic[0].counting().sortedBy { it.value }
            val list1 = statistic.resultStatistic[1].counting().sortedBy { it.value }

            println(list0)
            println(list1)
            println()
        }
        runBlocking {
            val tasks = TaskTimingImpl<Pair<UInt, UInt>, UInt>(
                Task("kotlin random UInt") { (from, until) -> Random.nextUInt(from, until) },
                Task("fast random UInt") { (from, until) -> Random.nextUIntFDR(from, until) }
            )
            val statistic = TaskResultStatistic(tasks)

            repeat(1000000) {
                statistic.go(23u to 105u)
            }
            val list0 = statistic.resultStatistic[0].counting().sortedBy { it.value }
            val list1 = statistic.resultStatistic[1].counting().sortedBy { it.value }

            println(list0)
            println(list1)
            println()
        }

        runBlocking {
            val tasks = TaskTimingImpl<UIntRange, UInt>(
                Task("kotlin random UInt") { range -> Random.nextUInt(range) },
                Task("fast random UInt") { range -> Random.nextUIntFDR(range) }
            )
            val statistic = TaskResultStatistic(tasks)

            repeat(1000000) {
                statistic.go(23u..105u)
            }
            val list0 = statistic.resultStatistic[0].counting().sortedBy { it.value }
            val list1 = statistic.resultStatistic[1].counting().sortedBy { it.value }

            println(list0)
            println(list1)
            println()
        }
    }

    @Test
    fun testRandomFactoredNumberDistribution() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("fast random UInt") { Random.nextUIntFDR(1u..it) },
                Task("random factored number") { runBlocking { Random.randomFactoredNumber(it).value } }
            )
            val statistic = TaskResultStatistic(tasks)

            repeat(1000000) {
                statistic.go(105u)
            }
            val list0 = statistic.resultStatistic[0].counting().sortedBy { it.value }
            val list1 = statistic.resultStatistic[1].counting().sortedBy { it.value }

            println(list0)
            println(list1)
            println()
        }
    }

    @Test
    fun testRandomFactoredNumberSpeed() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("fast random UInt") { Random.nextUIntFDR(it) },
                Task("random factored number") { runBlocking { Random.randomFactoredNumber(it).value } }
            )
            val statistic = TaskTimingStatistic(tasks)

            repeat(20000) {
//                println(it)
                statistic.go(Random.nextUInt())
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

//    lazyAssert() in FactorizationUInt already checking correctness of random factored number

}