package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.default_impl.DftMatrixBuilderImpl
import kotlinx.coroutines.runBlocking
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.ulong.primeOf
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTwoMatrixMultiplicationTiming
import math.timing.TwoMatrix
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/19 21:31
 *
 * test default implementation
 */
internal class DftMatrixTest {

    // *  : average 3.13101ms, deviation 4.41889ms
    // *p : average 3.40387ms, deviation 4.60127ms
    // * t: average 3.09033ms, deviation 4.48525ms
    // *pt: average 4.56294ms, deviation 4.77859ms
    //d*  : average 20.27439ms, deviation 19.80229ms
    //samples: 200, total time: 6.892508900s
    //range: 1..200
    @Test
    fun multiplication() {
        runBlocking {
            val defaultBuilder = DftMatrixBuilderImpl<ModularUInt>()

            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            val range = 1u..200u
            for (i in range) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val dft = defaultBuilder.compute(primeField.firstGenerator)
                val x = primeField.randomMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            println("range: $range")
        }
    }

    //faster again
    // *  : average 81.87877ms, deviation 91.55226ms
    // *p : average 77.67096ms, deviation 90.56825ms
    // * t: average 76.86687ms, deviation 92.17809ms
    // *pt: average 86.83731ms, deviation 89.01588ms
    //d*  : average 381.16559ms, deviation 13.39501ms
    //samples: 11, total time: 7.748614600s
    //range: 410..420
    @Test
    fun largeMultiplication() {
        runBlocking {
            val defaultBuilder = DftMatrixBuilderImpl<ModularUInt>()

            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            val range = 410u..420u
            for (i in range) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val dft = defaultBuilder.compute(primeField.firstGenerator)
                val x = primeField.randomMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            println("range: $range")
        }
    }


}