package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double

import cryptography.lattice.ring_lwe.ring.RootUIntP
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.FieldComplexNumberDouble
import math.integer.uint.factored.primeFactorization
import math.integer.ulong.primeOf
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

    //more accurate now
    //4.834323559512028E-7
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

    //slower than DftMatrix on Z/(p) ?
    //a bit slower, but more accurate
    // *  : average 16.02021ms, deviation 13.62026ms
    // *p : average 16.24256ms, deviation 13.34189ms
    // * t: average 15.94355ms, deviation 13.51269ms
    // *pt: average 17.34704ms, deviation 13.92480ms
    //d*  : average 34.55805ms, deviation 34.59322ms
    //total: 20.022283700s
    //average: 7.83331343514978E-8, max: 5.585313277043764E-6
    //range: 1..200
    @Test
    fun multiplication() {
        runBlocking {
            val statistic = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())
            val range = 1u..200u
            for (i in range) {
                val prime = primeOf(i).toUInt()
                val dft = FieldComplexNumberDouble.dft((prime - 1u).primeFactorization())
                val x = FieldComplexNumberDouble.randomMatrix(dft.columns, 2u, prime.toDouble())
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            statistic.printAverageAndMaxDistance()
            println("range: $range")
        }
    }

    //a bit slower, but more accurate and time-stable
    // *  : average 117.38925ms, deviation 34.52243ms
    // *p : average 107.07288ms, deviation 20.28890ms
    // * t: average 104.31870ms, deviation 21.82223ms
    // *pt: average 117.18685ms, deviation 17.67930ms
    //d*  : average 721.74754ms, deviation 26.41343ms
    //total: 12.844867400s
    //average distance: 1.7458237254970154E-5, max distance: 7.53154128323864E-5
    //range 410..420
    @Test
    fun largeMultiplication() {
        runBlocking {
            val statistic = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())
            val range = 410u..420u
            for (i in range) {
                val prime = primeOf(i).toUInt()
                val dft = FieldComplexNumberDouble.dft((prime - 1u).primeFactorization())
                val x = FieldComplexNumberDouble.randomMatrix(dft.columns, 2u, prime.toDouble())
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            statistic.printAverageAndMaxDistance()
            println("range $range")
        }
    }

    //a bit faster, more accurate and time-stable
    // *  : average 5.59187ms, deviation 11.93975ms
    // *p : average 8.45663ms, deviation 16.88845ms
    // * t: average 5.53166ms, deviation 11.94401ms
    // *pt: average 8.44935ms, deviation 16.91003ms
    //d*  : average 8.05969ms, deviation 29.65498ms
    //total: 17.467171600s
    //average distance: 1.94412666578443E-6, max distance: 7.512332691283856E-5
    //range: 1..484
    @Test
    fun primeField() {
        runBlocking {
            val statistic = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())
            val range = 1u..484u
            for (i in range) {
                val prime = primeOf(i)
                val root = FieldComplexNumberDouble.root((prime.toUInt() - 1u).primeFactorization())
                val dft = when (root) {
                    is RootUIntPPP -> PrimeDftMatrixComplexDouble(root.primeSubrootAt(root.order.factors.size.toUInt() - 1u))
                    is RootUIntPP  -> PrimeDftMatrixComplexDouble(root.primeSubroot())
                    is RootUIntP   -> PrimeDftMatrixComplexDouble(root)
                    else           -> error("unknown type of root $root, class: ${root::class}")
                }
                val x = FieldComplexNumberDouble.randomMatrix(dft.columns, 2u, prime.toDouble())
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            statistic.printAverageAndMaxDistance()
            println("range: $range")
        }
    }

    //a bit faster, more accurate and time-stable
    // *  : average 12.05132ms, deviation 19.84043ms
    // *p : average 17.97778ms, deviation 28.54891ms
    // * t: average 11.23950ms, deviation 19.00349ms
    // *pt: average 17.11428ms, deviation 27.25500ms
    //d*  : average 26.00565ms, deviation 62.26409ms
    //total: 7.173024600s
    //average distance: 6.78593196544304E-6, max distance: 6.983699626707115E-5
    //range: 400..484
    @Test
    fun largePrimeField2() {
        runBlocking {
            val statistic = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())
            val range = 400u..484u
            for (i in range) {
                val prime = primeOf(i)
                val root = FieldComplexNumberDouble.root((prime.toUInt() - 1u).primeFactorization())
                val dft = when (root) {
                    is RootUIntPPP -> PrimeDftMatrixComplexDouble(root.primeSubrootAt(root.order.factors.size.toUInt() - 1u))
                    is RootUIntPP  -> PrimeDftMatrixComplexDouble(root.primeSubroot())
                    is RootUIntP   -> PrimeDftMatrixComplexDouble(root)
                    else           -> error("unknown type of root $root, class: ${root::class}")
                }
                val x = FieldComplexNumberDouble.randomMatrix(dft.columns, 2u, prime.toDouble())
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
            statistic.printAverageAndMaxDistance()
            println("range: $range")
        }
    }
}