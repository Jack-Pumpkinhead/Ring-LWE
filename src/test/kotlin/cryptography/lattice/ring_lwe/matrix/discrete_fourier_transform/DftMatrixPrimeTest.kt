package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.RootUIntP
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import kotlinx.coroutines.runBlocking
import math.integer.uint.factored.PrimeUInt
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.ulong.primeOf
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTwoMatrixMultiplicationTiming
import math.timing.TwoMatrix
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/19 23:13
 */
internal class DftMatrixPrimeTest {

    suspend fun testBase(range: UIntRange) {
        val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
        for (i in range) {
            val primeField = PrimeUInt(primeOf(i).toUInt()).primeField
            val root = primeField.firstGenerator
            val dft = when (root) {
                is RootUIntPPP -> DftMatrixP(root.primeSubrootAt(root.order.factors.size.toUInt() - 1u))
                is RootUIntPP  -> DftMatrixP(root.primeSubroot())
                is RootUIntP   -> DftMatrixP(root)
                else           -> error("unknown type of root $root, class: ${root::class}")
            }
            val x = primeField.randomMatrix(dft.columns, 2u)
            statistic.go(TwoMatrix(dft, x))
        }
        statistic.printAverageAndStandardDeviation()
        println("range: $range")
    }

    //significantly faster and time-stable (because it simply uses direct method of multiplication)
    // *  : average 4.82089ms, deviation 16.99625ms
    // *p : average 4.84466ms, deviation 16.04897ms
    // * t: average 4.31457ms, deviation 15.03940ms
    // *pt: average 4.67490ms, deviation 15.69691ms
    //d*  : average 4.76275ms, deviation 16.88134ms
    //total: 11.708879700s
    //range: 1..500
    @Test
    fun primeField() = runBlocking {
        testBase(1u..500u)
    }

    // *  : average 26.86529ms, deviation 45.49140ms
    // *p : average 28.79082ms, deviation 50.25456ms
    // * t: average 23.34681ms, deviation 38.06658ms
    // *pt: average 25.86361ms, deviation 41.90384ms
    //d*  : average 23.62794ms, deviation 37.46234ms
    //total: 1.284944700s
    //range: 1500..1509
    @Test
    fun largePrimeField() = runBlocking {
        testBase(1500u..1509u)
    }

    //i: 1510,  prime: 12647,  p-1: (12646 = [2, 6323])
    // *  : average 1691.92090ms, deviation 6.03883ms
    // *p : average 1763.78335ms, deviation 67.62663ms
    // * t: average 1791.79095ms, deviation 39.79519ms
    // *pt: average 1756.11605ms, deviation 39.05952ms
    //d*  : average 1741.48115ms, deviation 3.27977ms
    //total: 17.490184800s
    @Test
    fun largePrimeField1() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1510u..1510u) {   //1510 make computation very slow
                val prime = primeOf(i)
                val primeField = FieldModularUInt(prime.toUInt())
                println("i: $i,  prime: $prime,  p-1: ${primeField.primeMinusOne}")
                val root = primeField.firstGenerator
                val dft = when (root) {
                    is RootUIntPPP -> DftMatrixP(root.primeSubrootAt(root.order.factors.size.toUInt() - 1u))
                    is RootUIntPP  -> DftMatrixP(root.primeSubroot())
                    is RootUIntP   -> DftMatrixP(root)
                    else           -> error("unknown type of root $root, class: ${root::class}")
                }
                val x = primeField.randomMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
                statistic.go(TwoMatrix(dft, x)) //repeat to prevent exception in statistic, but cost double time.
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

}