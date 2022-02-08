package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import kotlinx.coroutines.runBlocking
import math.integer.FactorizationUIntPrime
import math.integer.modular.ModularUInt
import math.integer.nextTwoPower
import math.integer.primeOf
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTwoMatrixMultiplicationTiming
import math.timing.TwoMatrix
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/8 12:04
 */
internal class DftMatrixPrimeModularUIntTest {


    //slower than direct multiplication

    // *  : average 6.50177ms, deviation 14.39590ms
    // *p : average 10.47677ms, deviation 21.43642ms
    // * t: average 6.43536ms, deviation 14.31773ms
    // *pt: average 10.32301ms, deviation 21.13180ms
    //d*  : average 4.28885ms, deviation 15.18387ms
    //total: 19.012878102s  //why passed i in 1u .. 500u many times?

    // *  : average 6.29396ms, deviation 14.03700ms
    // *p : average 10.41247ms, deviation 20.80930ms
    // * t: average 6.22720ms, deviation 13.99684ms
    // *pt: average 10.25501ms, deviation 20.73427ms
    //d*  : average 4.02150ms, deviation 14.27738ms
    //total: 18.009708790s
    @Test
    fun primeField() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1u..450u) {
                val primeField = FactorizationUIntPrime(primeOf(i).toUInt()).primeField
                val root = primeField.firstGenerator
                val dft = DftMatrixPrimeModularUInt(root.primeSubroot(root.order.factors.size.toUInt() - 1u))
                val x = primeField.randomMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    //slower than direct multiplication

    // *  : average 13.12891ms, deviation 22.25002ms
    // *p : average 20.54678ms, deviation 32.81915ms
    // * t: average 12.50280ms, deviation 21.57349ms
    // *pt: average 19.74194ms, deviation 31.65519ms
    //d*  : average 11.80772ms, deviation 27.14546ms
    //total: 6.606893008s
    @Test
    fun largePrimeField2() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 400u..484u) {
                val prime = primeOf(i)
                val primeField = FactorizationUIntPrime(prime.toUInt()).primeField
//                println("i: $i,  prime: $prime")
                val root = primeField.firstGenerator
                val dft = DftMatrixPrimeModularUInt(root.primeSubroot(root.order.factors.size.toUInt() - 1u))
                val x = primeField.randomMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }


    //cannot perform large prime field DFT with Double precision FFT
    /*@Test
    fun largePrimeField() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1500u..1509u) {
                val primeField = FactorizationUIntPrime(primeOf(i).toUInt()).primeField
                val root = primeField.firstGenerator
                val dft = DftMatrixPrimeModularUInt(root.primeSubroot(root.order.factors.size.toUInt() - 1u))
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }*/

    //TODO currently, 'fast' prime field DFT is slower than normal method at i<=484, and have an unacceptable rounding error at i>485

    //situations that can avoid using padding method is very few
    @Test
    fun twoPower() {
        runBlocking {
            for (i in 0u..1000000u) {
                val prime = primeOf(i)
                val primeDec = prime.toUInt() - 1u
                val nextTwoPower = nextTwoPower(primeDec)
                if (nextTwoPower.value == primeDec) {
                    println("$prime - 1 = ${nextTwoPower.prime}^${nextTwoPower.power}")
                }
            }
        }
    }

}