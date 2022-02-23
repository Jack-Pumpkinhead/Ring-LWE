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
    // *  : average 3.34270ms, deviation 5.22216ms
    // *p : average 3.41590ms, deviation 4.56888ms
    // * t: average 3.18938ms, deviation 4.68262ms
    // *pt: average 4.64030ms, deviation 4.83911ms
    //d*  : average 17.59572ms, deviation 16.90608ms
    //total: 6.436801200s
    //range: 1..200
    @Test
    fun multiplication() {
        runBlocking {
            val defaultBuilder = DftMatrixPPIBuilderImpl<ModularUInt>()

            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            val range = 1u..200u
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

    //no significant improvement
    // *  : average 71.54746ms, deviation 79.70279ms
    // *p : average 68.98391ms, deviation 77.77547ms
    // * t: average 66.16415ms, deviation 79.31625ms
    // *pt: average 76.13909ms, deviation 78.67357ms
    //d*  : average 327.13438ms, deviation 11.44249ms
    //total: 6.709659s
    //range: 410..420

    //why become slower?
    // *  : average 133.89704ms, deviation 160.14149ms
    // *p : average 130.92723ms, deviation 157.77963ms
    // * t: average 130.25979ms, deviation 161.08258ms
    // *pt: average 139.01604ms, deviation 157.34762ms
    //d*  : average 380.71799ms, deviation 10.28197ms
    //samples: 11, total time: 10.062999s
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