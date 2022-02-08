package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double

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
    // *  : average 15.29855ms, deviation 13.34968ms
    // *p : average 15.48522ms, deviation 13.10551ms
    // * t: average 15.13052ms, deviation 13.30079ms
    // *pt: average 16.62362ms, deviation 13.59304ms
    //d*  : average 33.41120ms, deviation 33.38036ms
    //total: 19.189820594s
    //average: 6.837927288466171E-7, max: 5.912274465473463E-5
    @Test
    fun multiplication() {
        runBlocking {
            val statistic = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())
            for (i in 1u..200u) {
                val prime = primeOf(i).toUInt()
                val dft = FieldComplexNumberDouble.dft((prime - 1u).primeFactorization())
                val x = FieldComplexNumberDouble.randomMatrix(dft.columns, 2u, prime.toDouble())
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            statistic.printAverageAndMaxDistance()
        }
    }

    //faster than direct multiplication of DftMatrix on Z/(p)
    //and parallel method started to work.
    // *  : average 116.59528ms, deviation 37.37676ms
    // *p : average 106.49788ms, deviation 26.72946ms
    // * t: average 103.74234ms, deviation 29.93683ms
    // *pt: average 117.40474ms, deviation 24.43515ms
    //d*  : average 671.86108ms, deviation 19.47628ms
    //total: 12.277114507s
    //average: 1.8593447047501372E-4, max: 8.379635427682263E-4
    @Test
    fun largeMultiplication() {
        runBlocking {
            val statistic = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())
            for (i in 410u..420u) {
                val prime = primeOf(i).toUInt()
                val dft = FieldComplexNumberDouble.dft((prime - 1u).primeFactorization())
                val x = FieldComplexNumberDouble.randomMatrix(dft.columns, 2u, prime.toDouble())
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            statistic.printAverageAndMaxDistance()
        }
    }

    // *  : average 5.94735ms, deviation 13.33431ms
    // *p : average 9.72825ms, deviation 19.85498ms
    // * t: average 5.97808ms, deviation 13.33069ms
    // *pt: average 9.72660ms, deviation 19.96243ms
    //d*  : average 7.53727ms, deviation 27.36269ms
    //total: 18.836094401s
    //average: 1.9132886893372178E-5, max: 6.868382116736388E-4
    @Test
    fun primeField() {
        runBlocking {
            val statistic = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())
            for (i in 1u..484u) {
                val prime = primeOf(i)
                val root = FieldComplexNumberDouble.root((prime.toUInt() - 1u).primeFactorization())
                val dft = DftMatrixPrimeComplexDouble(root.primeSubroot(root.order.factors.size.toUInt() - 1u))
                val x = FieldComplexNumberDouble.randomMatrix(dft.columns, 2u, prime.toDouble())
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            statistic.printAverageAndMaxDistance()
        }
    }

    // *  : average 14.87212ms, deviation 25.43669ms
    // *p : average 22.85050ms, deviation 37.31633ms
    // * t: average 14.23107ms, deviation 24.73446ms
    // *pt: average 21.62239ms, deviation 35.18192ms
    //d*  : average 23.01333ms, deviation 54.05985ms
    //total: 8.210099824s
    //average: 6.996785315210394E-5, max: 7.612033504292067E-4
    @Test
    fun largePrimeField2() {
        runBlocking {
            val statistic = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())
            for (i in 400u..484u) {
                val prime = primeOf(i)
                val root = FieldComplexNumberDouble.root((prime.toUInt() - 1u).primeFactorization())
                val dft = DftMatrixPrimeComplexDouble(root.primeSubroot(root.order.factors.size.toUInt() - 1u))
                val x = FieldComplexNumberDouble.randomMatrix(dft.columns, 2u, prime.toDouble())
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            statistic.printAverageAndMaxDistance()
        }
    }
}