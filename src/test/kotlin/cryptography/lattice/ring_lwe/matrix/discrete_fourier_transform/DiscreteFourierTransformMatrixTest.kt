package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete.dft
import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.FieldModularUInt
import math.integer.modular.ModularUInt
import math.integer.primeOf
import math.random.randomModularUIntMatrix
import math.statistic.TaskTimingStatistic
import math.timing.TwoMatrix
import math.timing.TwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/19 21:31
 */
internal class DiscreteFourierTransformMatrixTest {

    //    10.65s
    // *  : average 8.69556ms, deviation 17.98244ms
    @Test
    fun multiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(TwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1u..200u) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val dft = primeField.dft()
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    // 15.61s
    // *  : average 273.63922ms, deviation 367.84189ms
    @Test
    fun largeMultiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(TwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 410u..420u) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val dft = primeField.dft()
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }


}