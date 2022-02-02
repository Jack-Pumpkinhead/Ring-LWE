package math.martix

import com.ionspin.kotlin.bignum.integer.toBigInteger
import cryptography.lattice.ring_lwe.coding.*
import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.RingBigInteger
import math.abstract_structure.instance.RingUInt
import math.coding.permutation.SwitchIndexPermutation
import math.isPairwiseCoprimeUInt
import math.operation.product
import math.random.randomUIntMatrix
import math.statistic.TaskTimingStatistic
import math.timing.TwoMatrix
import math.timing.TwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/16 22:16
 */
internal class PermutationMatrixTest {

    @Test
    fun multiplication() {
        runBlocking {
            val samples = mutableListOf<Pair<List<UInt>, AbstractMatrix<UInt>>>()
            for (length in 2..5) {
                repeat(1000) {
                    val bound = List(length) { Random.nextUInt(2u..100u) }
                    val product = RingBigInteger.product(bound.map { it.toBigInteger() })
                    if (product <= 1000) {
                        if (bound.isPairwiseCoprimeUInt()) {
                            samples += bound to Random.randomUIntMatrix(product.uintValue(), 2u, 0u..100u)
                        }
                    }
                }
            }
            println("samples: ${samples.size}")

            suspend fun testPermutation(permutation: (List<UInt>) -> SwitchIndexPermutation, name: String) {
                println("permutation $name")
                val statistic = TaskTimingStatistic(TwoMatrixMultiplicationTiming<UInt>())
                for ((bound, x) in samples) {
                    statistic.go(TwoMatrix(PermutationMatrix(RingUInt, permutation(bound)), x))
                }
                statistic.printAverageAndStandardDeviation()
                println()
            }

            testPermutation({ bound -> permCRInv(bound) }, "CR")
            testPermutation({ bound -> permRCInv(bound) }, "RC")
            testPermutation({ bound -> permRLInv(bound) }, "RL")
            testPermutation({ bound -> permLRInv(bound) }, "LR")
            testPermutation({ bound -> permCLInv(bound) }, "CL")
            testPermutation({ bound -> permLCInv(bound) }, "LC")


        }
    }

//        val bound = listOf(8u, 9u, 5u)


}