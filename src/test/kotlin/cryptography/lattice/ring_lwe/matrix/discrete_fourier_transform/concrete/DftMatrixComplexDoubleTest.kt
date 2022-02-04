package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.FieldComplexNumberDouble
import math.integer.primeFactorization
import math.integer.primeOf
import math.operation.maxAbsoluteDistance
import math.operation.multiply
import math.random.randomMatrix
import math.statistic.TaskNearMatrixComplexDoubleStatistic
import math.timing.TwoMatrix
import math.timing.TwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/4 16:58
 */
internal class DftMatrixComplexDoubleTest {

    //5.29386263777262E-6
    @Test
    fun approximatelyEquals() {
        runBlocking {
            val prime = primeOf(1000u).toUInt()
            val dft = FieldComplexNumberDouble.dft((prime - 1u).primeFactorization())
            val x = FieldComplexNumberDouble.randomMatrix(dft.columns, 2u, 10.0)
            val m1 = dft * x
            val m2 = multiply(dft, x)
            println(maxAbsoluteDistance(m1, m2))
        }
    }

    //slower than DftMatrix on Z/(p)
    // *  : average 16.29752ms, deviation 13.98964ms
    // *p : average 16.44031ms, deviation 14.04635ms
    // * t: average 16.04624ms, deviation 14.07935ms
    // *pt: average 17.72277ms, deviation 14.35874ms
    //d*  : average 33.54643ms, deviation 33.52126ms
    //total: 20.010654800s
    //average: 7.397929337518881E-9, max: 5.147973871997159E-7
    @Test
    fun multiplication() {
        runBlocking {
            val statistic = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())
            for (i in 1u..200u) {
                val prime = primeOf(i).toUInt()
                val dft = FieldComplexNumberDouble.dft((prime - 1u).primeFactorization())
                val x = FieldComplexNumberDouble.randomMatrix(dft.columns, 2u, 10.0)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            statistic.printAverageAndMaxDistance()
        }
    }

    //faster than DftMatrix on Z/(p) now!
    //and parallel method started to work.
    // *  : average 117.76305ms, deviation 37.15359ms
    // *p : average 106.83181ms, deviation 24.36754ms
    // * t: average 104.31774ms, deviation 29.23314ms
    // *pt: average 122.37411ms, deviation 25.32018ms
    //d*  : average 673.86712ms, deviation 18.57547ms
    //total: 12.376692100s
    //average: 6.775370123834831E-7, max: 3.034870920035866E-6
    @Test
    fun largeMultiplication() {
        runBlocking {
            val statistic = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())
            for (i in 410u..420u) {
                val prime = primeOf(i).toUInt()
                val dft = FieldComplexNumberDouble.dft((prime - 1u).primeFactorization())
                val x = FieldComplexNumberDouble.randomMatrix(dft.columns, 2u, 10.0)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            statistic.printAverageAndMaxDistance()
        }
    }

}