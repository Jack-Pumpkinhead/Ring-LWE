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

    //    kotlin: average 0.00992ms, deviation 0.06845ms
    //       FDR: average 0.03004ms, deviation 0.78983ms
    //    bigFDR: average 0.02344ms, deviation 0.06807ms
    //TPM kotlin: average 3.59102ms, deviation 4.37874ms
    //TPM    FDR: average 3.43500ms, deviation 0.77487ms
    //TPM bigFDR: average 3.48422ms, deviation 0.78481ms
    //total: 10.573649500s
    //kotlin:   TPM is 361.9618x slower
    //   FDR:   TPM is 114.3399x slower
    //bigFDR:   TPM is 148.6187x slower
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

    //    kotlin: average 0.00925ms, deviation 0.07345ms
    //       FDR: average 0.03072ms, deviation 0.81478ms
    //    bigFDR: average 0.02196ms, deviation 0.07597ms
    //TPM kotlin: average 6.83762ms, deviation 6.44792ms
    //TPM    FDR: average 4.88890ms, deviation 1.90222ms
    //TPM bigFDR: average 5.00935ms, deviation 1.98834ms
    //total: 16.797801900s
    //kotlin:   TPM is 738.8029x slower
    //   FDR:   TPM is 159.1647x slower
    //bigFDR:   TPM is 228.1435x slower
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
                Task("    factored kotlin") { runBlocking { Random.nextFactoredNumber(it).value } },
                Task("    factored    FDR") { runBlocking { Random.nextFactoredNumberFDR(it).value } },
                Task("TPM             FDR") { TPMRandom.nextUIntFDR(1u..it) },
                Task("TPM factored kotlin") { runBlocking { TPMRandom.nextFactoredNumber(it).value } },
                Task("TPM factored    FDR") { runBlocking { TPMRandom.nextFactoredNumberFDR(it).value } },
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

    //large r have large ambiguity of time

    //r: 4253509286
    //                FDR: average 2.71243ms, deviation 8.53839ms
    //    factored kotlin: average 3.76657ms, deviation 5.65271ms
    //    factored    FDR: average 1.96541ms, deviation 2.95534ms
    //TPM             FDR: average 16.63316ms, deviation 42.43218ms
    //TPM factored kotlin: average 2768.79875ms, deviation 4591.15284ms
    //TPM factored    FDR: average 4675.54103ms, deviation 5697.84878ms
    //total: 1m 14.694173500s
    //            FDR:   TPM is 6.1322x slower
    //factored kotlin:   TPM is 735.0982x slower
    //factored    FDR:   TPM is 2378.9138x slower

    //r: 1969962444
    //                FDR: average 2.72600ms, deviation 8.59665ms
    //    factored kotlin: average 3.98701ms, deviation 6.57718ms
    //    factored    FDR: average 2.19120ms, deviation 3.40412ms
    //TPM             FDR: average 17.10029ms, deviation 43.00662ms
    //TPM factored kotlin: average 2636.20869ms, deviation 2106.75640ms
    //TPM factored    FDR: average 2613.78427ms, deviation 1709.73858ms
    //total: 52.759974600s
    //            FDR:   TPM is 6.2730x slower
    //factored kotlin:   TPM is 661.1994x slower
    //factored    FDR:   TPM is 1192.8552x slower

    //r: 595938349
    //                FDR: average 2.58082ms, deviation 8.13327ms
    //    factored kotlin: average 3.66285ms, deviation 3.94722ms
    //    factored    FDR: average 3.14896ms, deviation 3.39685ms
    //TPM             FDR: average 16.71797ms, deviation 42.51859ms
    //TPM factored kotlin: average 2301.55426ms, deviation 1759.84322ms
    //TPM factored    FDR: average 1900.46372ms, deviation 2192.35902ms
    //total: 42.281285800s
    //            FDR:   TPM is 6.4778x slower
    //factored kotlin:   TPM is 628.3507x slower
    //factored    FDR:   TPM is 603.5211x slower
    @Test
    fun testRandomFactoredNumberSpeed() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("                FDR") { Random.nextUIntFDR(1u..it) },
                Task("    factored kotlin") { runBlocking { Random.nextFactoredNumber(it).value } },
                Task("    factored    FDR") { runBlocking { Random.nextFactoredNumberFDR(it).value } },
                Task("TPM             FDR") { TPMRandom.nextUIntFDR(1u..it) },
                Task("TPM factored kotlin") { runBlocking { TPMRandom.nextFactoredNumber(it).value } },
                Task("TPM factored    FDR") { runBlocking { TPMRandom.nextFactoredNumberFDR(it).value } },
            )
            val statistic = TaskTimingStatistic(tasks)

//            val r = Random.nextUInt(1u..UInt.MAX_VALUE)   //IDEA: this range is empty???
//            val r = Random.nextUInt()
            val r = 4253509286u
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

    //                FDR: average 0.28747ms, deviation 2.79365ms
    //    factored kotlin: average 0.25509ms, deviation 0.80702ms
    //    factored    FDR: average 0.20759ms, deviation 0.65087ms
    //TPM             FDR: average 4.73363ms, deviation 15.47228ms
    //TPM factored kotlin: average 92.28323ms, deviation 80.79305ms
    //TPM factored    FDR: average 114.85421ms, deviation 101.07840ms
    //total: 21.262121s
    //            FDR:   TPM is 16.4667x slower
    //factored kotlin:   TPM is 361.7744x slower
    //factored    FDR:   TPM is 553.2716x slower
    @Test
    fun testSmallRandomFactoredNumberSpeed() {
        runBlocking {
            val tasks = TaskTimingImpl<UInt, UInt>(
                Task("                FDR") { Random.nextUIntFDR(1u..it) },
                Task("    factored kotlin") { runBlocking { Random.nextFactoredNumber(it).value } },
                Task("    factored    FDR") { runBlocking { Random.nextFactoredNumberFDR(it).value } },
                Task("TPM             FDR") { TPMRandom.nextUIntFDR(1u..it) },
                Task("TPM factored kotlin") { runBlocking { TPMRandom.nextFactoredNumber(it).value } },
                Task("TPM factored    FDR") { runBlocking { TPMRandom.nextFactoredNumberFDR(it).value } },
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