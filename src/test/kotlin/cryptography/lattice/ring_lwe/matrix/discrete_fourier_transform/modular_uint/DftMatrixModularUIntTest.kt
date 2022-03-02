package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import kotlinx.coroutines.runBlocking
import math.complex_number.maxRoundingError
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.ulong.primeOf
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTwoMatrixMultiplicationTiming
import math.timing.TwoMatrix
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Created by CowardlyLion at 2022/2/5 13:09
 */
internal class DftMatrixModularUIntTest {

    //outperform ordinary method now.
    // *  : average 2.04940ms, deviation 2.52790ms
    // *p : average 2.17355ms, deviation 2.46768ms
    // * t: average 1.96047ms, deviation 2.55947ms
    // *pt: average 3.49734ms, deviation 2.79450ms
    //d*  : average 17.41303ms, deviation 16.83532ms
    //samples: 200, total time: 5.418757900s
    //range: 1..200
    @Test
    fun multiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            val range = 1u..200u
            for (i in range) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val dft = primeField.firstFullDftFast()
                val x = primeField.randomMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            println("range: $range")
        }
    }

    //outperform ordinary method now.
    // *  : average 54.33816ms, deviation 66.72178ms
    // *p : average 43.60698ms, deviation 44.07210ms
    // * t: average 41.37795ms, deviation 44.65537ms
    // *pt: average 52.01183ms, deviation 44.30465ms
    //d*  : average 358.72960ms, deviation 33.35449ms
    //samples: 11, total time: 6.050709800s
    //range: 410..420
    @Test
    fun largeMultiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            val range = 410u..420u
            for (i in range) {
                val prime = primeOf(i)
                val primeField = FieldModularUInt(prime.toUInt())
                println("prime: $prime")
                val dft = primeField.firstFullDftFast()
                val x = primeField.randomMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            println("range: $range")
        }
    }


    //ends at i:485,  prime: 3467,  err: 0.3245372772216797
    //i: 901,  prime: 7013,  err: -0.3325347900390625   //may significantly larger than 1
    //i: 1000,  prime: 7927,  err: 0.3326225280761719
    @Test
    fun largeMultiplication1() {
        runBlocking {
            val exception = assertThrows<IllegalArgumentException> {
                val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
//                for (i in 1u..901u) {
                for (i in 745u..9001u) {    //TODO test maximal acceptable DFT
                    val prime = primeOf(i)
                    val primeField = FieldModularUInt(prime.toUInt())
                    println("i: $i,  prime: $prime, p-1: ${primeField.primeMinusOne}")
                    val dft = primeField.firstFullDftFast()
                    val x = primeField.randomMatrix(dft.columns, 2u)

                    maxRoundingError = 0.0
                    statistic.go(TwoMatrix(dft, x))
                    println("maxRoundingError: $maxRoundingError")
                }
                statistic.printAverageAndStandardDeviation()
            }
            exception.printStackTrace()
        }
    }


    //TODO draw a algorithm time graph with different method (direct/fast, ordinary/parallel, ...), use fastest method at different level.

}