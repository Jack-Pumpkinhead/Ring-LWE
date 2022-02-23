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

    //a bit slower than DftMatrix on Z/(p), but significantly faster now
    // *  : average 4.63115ms, deviation 8.38823ms
    // *p : average 4.62763ms, deviation 6.61297ms
    // * t: average 4.18084ms, deviation 6.04738ms
    // *pt: average 5.65603ms, deviation 6.41442ms
    //d*  : average 33.34370ms, deviation 33.75852ms
    //samples: 200, total time: 10.487868600s
    //average distance: 4.285146091883088E-8, max distance: 5.117668648580157E-6
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

    //significantly faster now, only a bit slower than DftMatrix on Z/(p)
    // *  : average 61.69466ms, deviation 56.74904ms
    // *p : average 54.90454ms, deviation 46.94890ms
    // * t: average 50.96729ms, deviation 47.14343ms
    // *pt: average 60.52852ms, deviation 45.81325ms
    //d*  : average 685.94943ms, deviation 20.15691ms
    //samples: 11, total time: 10.054488800s
    //average distance: 1.790625714556701E-5, max distance: 7.375587762754825E-5
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
    // *  : average 4.50272ms, deviation 12.64917ms
    // *p : average 5.39144ms, deviation 14.54141ms
    // * t: average 4.40415ms, deviation 12.27610ms
    // *pt: average 5.29099ms, deviation 14.41603ms
    //d*  : average 7.91845ms, deviation 28.78574ms
    //samples: 484, total time: 13.313753s
    //average distance: 1.8279345143600485E-6, max distance: 5.996043237606617E-5
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
    // *  : average 10.11894ms, deviation 19.37400ms
    // *p : average 12.18764ms, deviation 22.07281ms
    // * t: average 9.12951ms, deviation 17.13135ms
    // *pt: average 11.55292ms, deviation 21.07485ms
    //d*  : average 23.39107ms, deviation 55.49724ms
    //samples: 85, total time: 5.642306700s
    //average distance: 7.014583859750442E-6, max distance: 6.523318487683674E-5
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