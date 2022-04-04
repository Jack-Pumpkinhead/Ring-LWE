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
import util.test.FullPrimeFieldDftTestBase

/**
 * Created by CowardlyLion at 2022/2/5 13:09
 */
internal class DftMatrixModularUIntTest {

    //outperform ordinary method now.
    // *  : average 2.04583ms, deviation 2.64566ms
    // *p : average 2.18913ms, deviation 2.47556ms
    // * t: average 2.01559ms, deviation 2.48556ms
    // *pt: average 3.48090ms, deviation 2.88131ms
    //d*  : average 17.78645ms, deviation 17.31681ms
    //samples: 200, total time: 5.503582700s
    //range: 1..200
    @Test
    fun multiplication() = runBlocking {
        FullPrimeFieldDftTestBase(DftMatrixBuilderModularUInt).test(1u..200u)
    }

    //outperform ordinary method now.
    // *  : average 58.24326ms, deviation 79.74854ms
    // *p : average 43.99034ms, deviation 45.89975ms
    // * t: average 40.98181ms, deviation 46.95661ms
    // *pt: average 53.65272ms, deviation 46.42971ms
    //d*  : average 351.32254ms, deviation 22.61505ms
    //samples: 11, total time: 6.030097400s
    //range: 410..420
    @Test
    fun largeMultiplication() = runBlocking {
        FullPrimeFieldDftTestBase(DftMatrixBuilderModularUInt).test(410u..420u)
    }


    //it's significantly exact now
    //i: 2000,  prime: 17393, p-1: (17392 = [2^4, 1087])
    //maxRoundingError: 0.0
    //i: 2001,  prime: 17401, p-1: (17400 = [2^3, 3, 5^2, 29])
    //maxRoundingError: 0.0
    //i: 2002,  prime: 17417, p-1: (17416 = [2^3, 7, 311])
    //maxRoundingError: 0.0
    //i: 2003,  prime: 17419, p-1: (17418 = [2, 3, 2903])
    //maxRoundingError: 0.0
    //i: 2004,  prime: 17431, p-1: (17430 = [2, 3, 5, 7, 83])
    //maxRoundingError: 0.0

    //i: 8000,  prime: 81817, p-1: (81816 = [2^3, 3, 7, 487])
    //maxRoundingError: 0.0
    //i: 8001,  prime: 81839, p-1: (81838 = [2, 17, 29, 83])
    //maxRoundingError: 0.0
    //i: 8002,  prime: 81847, p-1: (81846 = [2, 3^2, 4547])
    //maxRoundingError: 0.001953125
    //i: 8003,  prime: 81853, p-1: (81852 = [2^2, 3, 19, 359])
    //maxRoundingError: 0.0
    //i: 8004,  prime: 81869, p-1: (81868 = [2^2, 97, 211])
    //maxRoundingError: 0.0
    //i: 8005,  prime: 81883, p-1: (81882 = [2, 3^2, 4549])
    @Test
    fun largeMultiplication1() {
        runBlocking {
            val exception = assertThrows<IllegalArgumentException> {
                val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
//                for (i in 1u..901u) {
//                for (i in 1578u..9001u) {    //TODO test maximal acceptable DFT
                for (i in 8000u..9001u) {    //TODO test maximal acceptable DFT
                    val prime = primeOf(i)
                    val primeField = FieldModularUInt(prime.toUInt())
                    println("i: $i,  prime: $prime, p-1: ${primeField.primeMinusOne}")
                    val dft = DftMatrixBuilderModularUInt.compute(primeField.firstGenerator)
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