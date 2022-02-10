package math.random

import org.junit.jupiter.api.Test
import util.stdlib.list
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/2/9 22:53
 */
internal class AlgorithmKtTest {

    //413ms
    @Test
    fun exponentialDistribution() {

        //java's Random may have a nextGaussian() method
        val numbers = list(10000u) {
            val x = Random.nextBinaryRandomNumberExponentialDistribution()
            x.toBigDecimal(10uL)
        }.sorted().map { it.toStringExpanded() }

        println("Histogram[${numbers.joinToString(prefix = "{", postfix = "}")}]")
    }

    //35s
    @Test
    fun exponentialDistribution1() {

        val numbers = list(2000u) {
            val x = TPMRandom.nextBinaryRandomNumberExponentialDistribution()
            x.toBigDecimal(10uL)
        }.sorted().map { it.toStringExpanded() }

        println("Histogram[${numbers.joinToString(prefix = "{", postfix = "}")}]")
    }


}