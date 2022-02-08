package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.FieldModularUInt
import math.integer.modular.ModularUInt
import math.integer.primeOf
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

    // *  : average 15.84204ms, deviation 13.65451ms
    // *p : average 16.08606ms, deviation 13.47130ms
    // * t: average 15.88578ms, deviation 13.91805ms
    // *pt: average 17.44031ms, deviation 14.55987ms
    //d*  : average 17.39220ms, deviation 17.30109ms
    //total: 16.529278395s
    @Test
    fun multiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1u..200u) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val dft = primeField.firstFullDftFast()
                val x = primeField.randomMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    // *  : average 130.29925ms, deviation 49.46187ms
    // *p : average 115.82274ms, deviation 25.38631ms
    // * t: average 115.81454ms, deviation 31.18571ms
    // *pt: average 124.87878ms, deviation 24.65770ms
    //d*  : average 325.86293ms, deviation 9.46035ms
    //total: 8.939460601s
    @Test
    fun largeMultiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 410u..420u) {
                val prime = primeOf(i)
                val primeField = FieldModularUInt(prime.toUInt())
//                println("prime: $prime")
                val dft = primeField.firstFullDftFast()
                val x = primeField.randomMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
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
                for (i in 460u..901u) {
                    val prime = primeOf(i)
                    val primeField = FieldModularUInt(prime.toUInt())
                    println("i: $i,  prime: $prime")
                    val dft = primeField.firstFullDftFast()
                    val x = primeField.randomMatrix(dft.columns, 2u)
                    statistic.go(TwoMatrix(dft, x))
                }
                statistic.printAverageAndStandardDeviation()
            }
            exception.printStackTrace()
        }
    }


    //TODO draw a algorithm time graph with different method (direct/fast, ordinary/parallel, ...), use fastest method at different level.

}