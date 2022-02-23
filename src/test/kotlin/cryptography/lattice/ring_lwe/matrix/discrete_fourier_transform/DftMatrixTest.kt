package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import kotlinx.coroutines.runBlocking
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.ulong.primeOf
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.TwoMatrix
import math.timing.EqualTwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/19 21:31
 */
internal class DftMatrixTest {

    //no significant improvement
    // *  : average 3.18263ms, deviation 4.62477ms
    // *p : average 3.36925ms, deviation 4.68172ms
    // * t: average 3.15762ms, deviation 4.73332ms
    // *pt: average 4.69827ms, deviation 5.65669ms
    //d*  : average 17.37290ms, deviation 16.76467ms
    //total: 6.356135100s
    @Test
    fun multiplication() {
        runBlocking {
            val defaultBuilder = DftMatrixPPIBuilderImpl<ModularUInt>()

            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1u..200u) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val dft = defaultBuilder.build(primeField.firstGenerator)
                val x = primeField.randomMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    //no significant improvement
    // *  : average 71.54746ms, deviation 79.70279ms
    // *p : average 68.98391ms, deviation 77.77547ms
    // * t: average 66.16415ms, deviation 79.31625ms
    // *pt: average 76.13909ms, deviation 78.67357ms
    //d*  : average 327.13438ms, deviation 11.44249ms
    //total: 6.709659s
    //range: 410..420
    @Test
    fun largeMultiplication() {
        runBlocking {
            val defaultBuilder = DftMatrixPPIBuilderImpl<ModularUInt>()

            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            val range = 410u..420u
            for (i in range) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val dft = defaultBuilder.build(primeField.firstGenerator)
                val x = primeField.randomMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            println("range: $range")
        }
    }


}