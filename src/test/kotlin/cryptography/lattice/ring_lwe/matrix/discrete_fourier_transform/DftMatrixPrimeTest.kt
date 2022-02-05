package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

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

/**
 * Created by CowardlyLion at 2022/1/19 23:13
 */
internal class DftMatrixPrimeTest {

    // *  : average 22.07341ms, deviation 82.52829ms
    // *p : average 22.46476ms, deviation 82.13925ms
    // * t: average 21.88885ms, deviation 81.65968ms
    // *pt: average 22.52631ms, deviation 83.01061ms
    //d*  : average 4.61775ms, deviation 16.37484ms
    //total: 46.785538850s
    @Test
    fun primeField() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1u..500u) {
                val primeField = FactorizationUIntPrime(primeOf(i).toUInt()).primeField
                val root = primeField.firstGenerator
//                val dft = DftMatrixPrime(root.primeSubroot(Random.nextUInt(root.order.factors.size.toUInt())))
                val dft = DftMatrixPrime(root.primeSubroot(root.order.factors.size.toUInt() - 1u))
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    // *  : average 123.94640ms, deviation 205.52942ms
    // *p : average 126.44313ms, deviation 207.64305ms
    // * t: average 120.12544ms, deviation 194.10459ms
    // *pt: average 122.22508ms, deviation 197.35755ms
    //d*  : average 23.26617ms, deviation 39.30466ms
    //total: 5.160062200s
    @Test
    fun largePrimeField() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1500u..1509u) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val root = primeField.firstGenerator
//                val dft = DftMatrixPrime(root.primeSubroot(Random.nextUInt(root.order.factors.size.toUInt())))    //too many variation here
                val dft = DftMatrixPrime(root.primeSubroot(root.order.factors.size.toUInt() - 1u))  //very large prime
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    //i: 1510,  prime: 12647 = 2*6323
    // *  : average 11082.49505ms, deviation 10.85161ms
    // *p : average 11223.82990ms, deviation 80.62629ms
    // * t: average 11138.98910ms, deviation 64.11125ms
    // *pt: average 11177.41415ms, deviation 90.70023ms
    //d*  : average 1659.52700ms, deviation 19.51162ms
    //total: 1m 32.564510402s
    @Test
    fun largePrimeField1() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1510u..1510u) {   //1510 make computation very slow
                val prime = primeOf(i)
                val primeField = FieldModularUInt(prime.toUInt())
                println("i: $i,  prime: $prime")
                val root = primeField.firstGenerator
//                val dft = DftMatrixPrime(root.primeSubroot(Random.nextUInt(root.order.factors.size.toUInt())))    //too many variation here
                val dft = DftMatrixPrime(root.primeSubroot(root.order.factors.size.toUInt() - 1u))  //very large prime
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
                statistic.go(TwoMatrix(dft, x)) //repeat to prevent exception in statistic, but cost another 45s.
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    // *  : average 64.17284ms, deviation 154.30660ms
    // *p : average 65.26600ms, deviation 155.17688ms
    // * t: average 63.91765ms, deviation 153.66498ms
    // *pt: average 65.44867ms, deviation 155.15627ms
    //d*  : average 12.53646ms, deviation 29.40070ms
    //total: 23.064037610s
    @Test
    fun largePrimeField2() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 400u..484u) {
                val prime = primeOf(i)
                val primeField = FieldModularUInt(prime.toUInt())
                println("i: $i,  prime: $prime")
                val root = primeField.firstGenerator
                val dft = DftMatrixPrime(root.primeSubroot(root.order.factors.size.toUInt() - 1u))  //very large prime
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

}