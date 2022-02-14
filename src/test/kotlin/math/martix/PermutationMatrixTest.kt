package math.martix

import com.ionspin.kotlin.bignum.integer.toBigInteger
import cryptography.lattice.ring_lwe.coding.*
import kotlinx.coroutines.runBlocking
import math.integer.big_integer.RingBigInteger
import math.integer.uint.RingUInt
import math.coding.permutation.SwitchIndexPermutation
import math.isPairwiseCoprimeUInt
import math.operation.product
import math.random.randomUIntMatrix
import math.statistic.TaskTimingStatistic
import math.timing.TwoMatrix
import math.timing.EqualTwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/16 22:16
 */
internal class PermutationMatrixTest {

    //BUILD SUCCESSFUL in 31s

    @Test
    fun multiplication() {
        runBlocking {
            data class Sample(val bound: UInt, val bounds: List<UInt>, val matrix: AbstractMatrix<UInt>)

            val samples = mutableListOf<Sample>()
            for (length in 2..5) {
                repeat(1000) {
                    val bound = List(length) { Random.nextUInt(2u..100u) }
                    val product = RingBigInteger.product(bound.map { it.toBigInteger() })
                    if (product <= 1000) {
                        if (bound.isPairwiseCoprimeUInt()) {
                            samples += Sample(product.uintValue(true), bound, Random.randomUIntMatrix(product.uintValue(), 2u, 0u..100u))
                        }
                    }
                }
            }
            println("samples: ${samples.size}")

            suspend fun testPermutation(permutation: (Sample) -> SwitchIndexPermutation, name: String) {
                println("permutation $name")
                val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<UInt>())
                for (sample in samples) {
                    statistic.go(TwoMatrix(PermutationMatrix(RingUInt, permutation(sample)), sample.matrix))
                }
                statistic.printAverageAndStandardDeviation()
                println()
            }

            testPermutation({ sample -> permCRInv(sample.bound, sample.bounds) }, "CR")
            testPermutation({ sample -> permRCInv(sample.bound, sample.bounds) }, "RC")
            testPermutation({ sample -> permRLInv(sample.bound, sample.bounds) }, "RL")
            testPermutation({ sample -> permLRInv(sample.bound, sample.bounds) }, "LR")
            testPermutation({ sample -> permCLInv(sample.bound, sample.bounds) }, "CL")
            testPermutation({ sample -> permLCInv(sample.bound, sample.bounds) }, "LC")


        }
    }

//        val bound = listOf(8u, 9u, 5u)


}