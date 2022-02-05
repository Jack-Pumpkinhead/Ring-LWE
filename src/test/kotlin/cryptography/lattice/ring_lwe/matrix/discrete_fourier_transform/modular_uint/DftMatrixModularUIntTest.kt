package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.FieldModularUInt
import math.integer.FactorizationUIntPrime
import math.integer.modular.ModularUInt
import math.integer.primeOf
import math.random.randomModularUIntMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTwoMatrixMultiplicationTiming
import math.timing.TwoMatrix
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Created by CowardlyLion at 2022/2/5 13:09
 */
internal class DftMatrixModularUIntTest {

    // *  : average 15.84204ms, deviation 13.65451ms
    // *p : average 16.08606ms, deviation 13.47130ms
    // * t: average 15.88578ms, deviation 13.91805ms
    // *pt: average 17.44031ms, deviation 14.55987ms
    //d*  : average 17.39220ms, deviation 17.30109ms
    //total: 16.529278395s
    @Test
    fun multiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1u..200u) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val dft = primeField.firstFullDftFast()
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    // *  : average 130.29925ms, deviation 49.46187ms
    // *p : average 115.82274ms, deviation 25.38631ms
    // * t: average 115.81454ms, deviation 31.18571ms
    // *pt: average 124.87878ms, deviation 24.65770ms
    //d*  : average 325.86293ms, deviation 9.46035ms
    //total: 8.939460601s
    @Test
    fun largeMultiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 410u..420u) {
                val prime = primeOf(i)
                val primeField = FieldModularUInt(prime.toUInt())
//                println("prime: $prime")
                val dft = primeField.firstFullDftFast()
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }


    //ends at i:485,  prime: 3467,  err: 0.3245372772216797
    //i: 901,  prime: 7013,  err: -0.3325347900390625   //may significantly larger than 1
    //i: 1000,  prime: 7927,  err: 0.3326225280761719
    @Test
    fun largeMultiplication1() {
        runBlocking {
            val exception = assertThrows<IllegalArgumentException> {
                val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
//                for (i in 1u..901u) {
                for (i in 450u..901u) {
                    val prime = primeOf(i)
                    val primeField = FieldModularUInt(prime.toUInt())
                    println("i: $i,  prime: $prime")
                    val dft = primeField.firstFullDftFast()
                    val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
                    statistic.go(TwoMatrix(dft, x))
                }
                statistic.printAverageAndStandardDeviation()
            }
            exception.printStackTrace()
        }
    }


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
            for (i in 1u..484u) {
                val primeField = FactorizationUIntPrime(primeOf(i).toUInt()).primeField
                val root = primeField.firstGenerator
                val dft = DftMatrixPrimeModularUInt(root.primeSubroot(root.order.factors.size.toUInt() - 1u))
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
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
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
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


    //TODO draw a algorithm time graph with different method (direct/fast, ordinary/parallel, ...), use fastest method at different level.
    //currently 'fast' prime field DFT is slower than normal method at i<=484, and have an unacceptable rounding error at i>485
}