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

    //significantly faster
    // *  : average 3.21996ms, deviation 4.57589ms
    // *p : average 3.45527ms, deviation 4.65399ms
    // * t: average 3.17135ms, deviation 4.59103ms
    // *pt: average 4.65567ms, deviation 4.87628ms
    //d*  : average 17.56838ms, deviation 17.14555ms
    //total: 6.414125s
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

    //significantly faster
    // *  : average 72.84144ms, deviation 79.81287ms
    // *p : average 68.53130ms, deviation 78.78803ms
    // * t: average 66.62584ms, deviation 79.57418ms
    // *pt: average 78.73923ms, deviation 78.84515ms
    //d*  : average 327.17575ms, deviation 13.31347ms
    //total: 6.753049200s
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