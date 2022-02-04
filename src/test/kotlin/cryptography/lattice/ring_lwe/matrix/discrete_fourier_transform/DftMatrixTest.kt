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

    // *  : average 8.89614ms, deviation 18.64866ms
    // *p : average 8.91292ms, deviation 18.03492ms
    // * t: average 8.88337ms, deviation 18.81482ms
    // *pt: average 10.14590ms, deviation 18.61726ms
    //d*  : average 17.18974ms, deviation 17.03461ms
    //total: 10.805615700s
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

    // *  : average 273.90904ms, deviation 366.09402ms
    // *p : average 265.17641ms, deviation 363.14921ms
    // * t: average 263.79704ms, deviation 369.35522ms
    // *pt: average 271.46791ms, deviation 362.20674ms
    //d*  : average 319.00025ms, deviation 5.81205ms
    //total: 15.326857100s
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