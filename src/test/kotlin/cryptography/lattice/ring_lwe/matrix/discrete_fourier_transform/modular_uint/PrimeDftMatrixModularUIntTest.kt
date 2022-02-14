package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import cryptography.lattice.ring_lwe.ring.RootUIntP
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import kotlinx.coroutines.runBlocking
import math.integer.uint.factored.PrimeUInt
import math.integer.uint.modular.ModularUInt
import math.integer.uint.nextTwoPositivePower
import math.integer.ulong.primeOf
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTwoMatrixMultiplicationTiming
import math.timing.TwoMatrix
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/8 12:04
 */
internal class PrimeDftMatrixModularUIntTest {

    suspend fun testBase(range: UIntRange) {
        val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())

        for (i in range) {
            val primeField = PrimeUInt(primeOf(i).toUInt()).primeField
            val root = primeField.firstGenerator
            val dft = when (root) {
                is RootUIntPPP -> PrimeDftMatrixModularUInt(root.primeSubrootAt(root.order.factors.size.toUInt() - 1u))
                is RootUIntPP  -> PrimeDftMatrixModularUInt(root.primeSubroot())
                is RootUIntP   -> PrimeDftMatrixModularUInt(root)
                else           -> error("unknown type of root $root, class: ${root::class}")
            }
            val x = primeField.randomMatrix(dft.columns, 2u)
            statistic.go(TwoMatrix(dft, x))
        }
        statistic.printAverageAndStandardDeviation()
        println("range: $range")
    }

    //slower than direct multiplication

    //a bit faster and time-stable  (direct multiplication consume more 1ms, now both method (*/d*) are competitive, but both slower than basic DftMatrix
    // *  : average 5.59060ms, deviation 12.17743ms
    // *p : average 8.67935ms, deviation 17.56562ms
    // * t: average 5.51091ms, deviation 12.22591ms
    // *pt: average 8.66108ms, deviation 17.84523ms
    //d*  : average 5.32639ms, deviation 19.21536ms
    //total: 15.195755s
    //range: 1..450

    // *  : average 6.09468ms, deviation 12.83567ms
    // *p : average 9.17993ms, deviation 18.11865ms
    // * t: average 6.12549ms, deviation 13.07738ms
    // *pt: average 9.15591ms, deviation 18.22367ms
    //d*  : average 5.67920ms, deviation 20.14225ms
    //total: 18.117602700s
    //range: 1..500
    @Test
    fun primeField() = runBlocking {
        testBase(1u..500u)
    }

    /*//cannot perform large prime field DFT with Double precision FFT
    @Test
    fun largePrimeField() = runBlocking {
        testBase(1500u..1509u)
    }*/

    //TODO currently, 'fast' prime field DFT is slower than normal method at i<=484, and have an unacceptable rounding error at i>485

    //situations that can avoid using padding method is very few
    @Test
    fun twoPower() {
        runBlocking {
            for (i in 2u..1000000u) {
                val prime = primeOf(i)
                val primeDec = prime.toUInt() - 1u
                val nextTwoPower = nextTwoPositivePower(primeDec)
                if (nextTwoPower.value == primeDec) {
                    println("$prime - 1 = ${nextTwoPower.prime}^${nextTwoPower.power}")
                }
            }
        }
    }

}