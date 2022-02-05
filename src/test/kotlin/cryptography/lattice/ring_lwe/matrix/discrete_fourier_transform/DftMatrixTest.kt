package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.FieldModularUInt
import math.integer.modular.ModularUInt
import math.integer.primeOf
import math.random.randomModularUIntMatrix
import math.statistic.TaskTimingStatistic
import math.timing.TwoMatrix
import math.timing.EqualTwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/19 21:31
 */
internal class DftMatrixTest {

    // *  : average 8.76939ms, deviation 18.15522ms
    // *p : average 9.02094ms, deviation 18.02072ms
    // * t: average 8.79241ms, deviation 18.32357ms
    // *pt: average 10.24140ms, deviation 18.27370ms
    //d*  : average 17.09975ms, deviation 16.79598ms
    //total: 10.784778411s
    @Test
    fun multiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1u..200u) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val dft = primeField.firstFullDft()
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    // *  : average 274.66916ms, deviation 367.08094ms
    // *p : average 268.31002ms, deviation 368.17655ms
    // * t: average 266.93755ms, deviation 373.58580ms
    // *pt: average 279.44295ms, deviation 375.23690ms
    //d*  : average 332.57835ms, deviation 9.08284ms
    //total: 15.641318297s
    @Test
    fun largeMultiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 410u..420u) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val dft = primeField.firstFullDft()
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }


}