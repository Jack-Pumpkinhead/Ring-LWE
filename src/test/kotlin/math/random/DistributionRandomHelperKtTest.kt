package math.random

import com.ionspin.kotlin.bignum.decimal.div
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.coroutines.runBlocking
import math.statistic.TaskTimingStatistic
import math.timing.Task
import math.timing.TaskTimingImpl
import org.junit.jupiter.api.Test
import util.stdlib.list
import util.stdlib.repeat
import kotlin.math.E
import kotlin.math.pow
import kotlin.random.Random
import kotlin.random.asJavaRandom

/**
 * Created by CowardlyLion at 2022/2/9 22:53
 */
internal class DistributionRandomHelperKtTest {

    //413ms
    @Test
    fun exponentialDistribution() {
        val numbers = list(10000u) {
            val x = Random.nextBinaryRandomNumberExponentialDistribution()
            x.absoluteValueBigDecimal(10uL)
        }.sorted().map { it.toStringExpanded() }

        println("Histogram[${numbers.joinToString(prefix = "{", postfix = "}")}]")
    }

    //75ms, but have negative number
    @Test
    fun exponentialDistributionJava() {
        val javaRandom = java.util.Random()
        val numbers = list(10000u) {
            javaRandom.nextExponential()
        }.sorted()

        println("Histogram[${numbers.joinToString(prefix = "{", postfix = "}")}]")
    }

    //35s
    @Test
    fun exponentialDistribution1() {
        val numbers = list(2000u) {
            val x = TPMRandom.nextBinaryRandomNumberExponentialDistribution()
            x.absoluteValueBigDecimal(10uL)
        }.sorted().map { it.toStringExpanded() }

        println("Histogram[${numbers.joinToString(prefix = "{", postfix = "}")}]")
    }

    //ratio: 0.60656569
    //e^(-1/2): 0.6065306597126334
    //BUILD SUCCESSFUL in 11s
    @Test
    fun bernoulli() {
        val trials = 100000000u
        var numTrue = 0u
//        var numFalse = 0u
        for (i in 1u..trials) {
            val bool = Random.nextBooleanSpecialBernoulli()
            if (bool) {
                numTrue++
            }
        }

        val ratio = numTrue.toDouble() / trials.toDouble()
        println("ratio: $ratio")

        val e1 = E.pow(-0.5)
        println("e^(-1/2): $e1")

    }

    //0.5s
    @Test
    fun standardNormalDistribution() {
        val numbers = list(10000u) {
            val x = Random.nextBinaryRandomNumberStandardNormalDistribution()
            x.toBigDecimal(10uL)
        }.sorted().map { it.toStringExpanded() }

        println("Histogram[${numbers.joinToString(prefix = "{", postfix = "}")}]")
    }

    @Test
    fun standardNormalDistributionJava() {
        val javaRandom = java.util.Random()
        val numbers = list(10000u) {
            javaRandom.nextGaussian()
        }.sorted()

        println("Histogram[${numbers.joinToString(prefix = "{", postfix = "}")}]")
    }

    //2min 6sec
    @Test
    fun standardNormalDistribution1() {
        val numbers = list(2000u) {
            val x = TPMRandom.nextBinaryRandomNumberStandardNormalDistribution()
            x.toBigDecimal(10uL)
        }.sorted().map { it.toStringExpanded() }

        println("Histogram[${numbers.joinToString(prefix = "{", postfix = "}")}]")
    }

    //     1303.6257: average 0.50006ms, deviation 1.60131ms
    //          java: average 0.00444ms, deviation 0.00484ms
    //TPM  1303.6257: average 69.31595ms, deviation 55.53951ms
    //TPM       java: average 8.41569ms, deviation 10.53382ms
    //total: 1m 18.236132100s
    @Test
    fun standardNormalDistributionSpeed() {
        runBlocking {
            val javaRandom = java.util.Random()
            val tpmJavaRandom = TPMRandom.asJavaRandom()
            val timing = TaskTimingImpl<Unit, Double>(
                Task("     1303.6257") { Random.nextBinaryRandomNumberStandardNormalDistribution().toBigDecimal(54uL).doubleValue(false) },
                Task("          java") { javaRandom.nextGaussian() },
                Task("TPM  1303.6257") { TPMRandom.nextBinaryRandomNumberStandardNormalDistribution().toBigDecimal(54uL).doubleValue(false) },
                Task("TPM       java") { tpmJavaRandom.nextGaussian() }
            )

            val statistic = TaskTimingStatistic(timing)

            repeat(1000u) {
                statistic.go(Unit)
            }
            statistic.printAverageAndStandardDeviation()
        }
    }


    @Test
    fun integerNormalDistribution() {
        val mu = (-11).toBigDecimal()
        val sigma = 10.toBigDecimal()
        val list = list(100000u) {
            Random.nextIntegerNormalDistribution(mu, sigma, 1 / sigma)
        }
//        println(list.countingBigInteger().sortedBy { it.value })
        println("Histogram[${list.joinToString(prefix = "{", postfix = "}")}]")
    }
}