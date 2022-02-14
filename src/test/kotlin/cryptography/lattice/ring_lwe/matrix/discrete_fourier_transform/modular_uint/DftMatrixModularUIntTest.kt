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

    //a bit slower and time-unstable
    // *  : average 16.63000ms, deviation 14.25035ms
    // *p : average 16.73985ms, deviation 14.12499ms
    // * t: average 16.60502ms, deviation 14.32827ms
    // *pt: average 18.11378ms, deviation 14.64732ms
    //d*  : average 20.97162ms, deviation 21.00202ms
    //total: 17.812052600s
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

    //a bit slower and time-unstable
    // *  : average 121.97342ms, deviation 44.15251ms
    // *p : average 107.92397ms, deviation 20.24315ms
    // * t: average 106.28782ms, deviation 19.44385ms
    // *pt: average 116.73839ms, deviation 20.18821ms
    //d*  : average 385.00060ms, deviation 12.82751ms
    //total: 9.217166200s
    //range: 410..420
    @Test
    fun largeMultiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            val range = 410u..420u
            for (i in range) {
                val prime = primeOf(i)
                val primeField = FieldModularUInt(prime.toUInt())
//                println("prime: $prime")
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
                for (i in 700u..9001u) {    //TODO test maximal acceptable DFT
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