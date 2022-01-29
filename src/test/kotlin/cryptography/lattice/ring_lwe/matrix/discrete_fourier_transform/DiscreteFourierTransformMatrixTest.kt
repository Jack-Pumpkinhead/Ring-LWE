package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete.dft
import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.FieldModularUInt
import math.integer.modular.ModularUInt
import math.integer.primeOf
import math.random.randomModularUIntMatrix
import math.statistic.RepeatTaskStatistic
import math.timing.TwoMatrix
import math.timing.TwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/19 21:31
 */
internal class DiscreteFourierTransformMatrixTest {

    //    39s
    @Test
    fun multiplication() {
        runBlocking {
            val statistic = RepeatTaskStatistic(TwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1u..250u) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val dft = primeField.dft()
                val x = primeField.randomModularUIntMatrix(dft.columns, 1u..3u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    //    44s, parallel method faster than normal method now
    @Test
    fun largeMultiplication() {
        runBlocking {
            val statistic = RepeatTaskStatistic(TwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 400u..420u) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val dft = primeField.dft()
                val x = primeField.randomModularUIntMatrix(dft.columns, 1u..3u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }


}