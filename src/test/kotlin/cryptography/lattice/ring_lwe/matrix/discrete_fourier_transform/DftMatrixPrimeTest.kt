package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.FieldModularUInt
import math.integer.FactorizationUIntPrime
import math.integer.modular.ModularUInt
import math.integer.primeOf
import math.random.randomModularUIntMatrix
import math.statistic.TaskTimingStatistic
import math.timing.TwoMatrix
import math.timing.EqualTwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/19 23:13
 */
internal class DftMatrixPrimeTest {

    //i in 1u..500u
    // *  : average 8.58532ms, deviation 54.23853ms
    // *p : average 8.82498ms, deviation 54.83395ms
    // * t: average 8.60195ms, deviation 54.58314ms
    // *pt: average 8.79870ms, deviation 54.91579ms
    //d*  : average 1.78630ms, deviation 10.77970ms
    //total: 18.298630800s
    @Test
    fun primeField() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1u..500u) {
                val primeField = FactorizationUIntPrime(primeOf(i).toUInt()).primeField
                val root = primeField.firstGenerator
                val dft = DftMatrixPrime(root.primeSubroot(Random.nextUInt(root.order.factors.size.toUInt())))
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

    /*@Test
    fun largePrimeField1() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1510u..1510u) {   //1510 make computation very slow
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val root = primeField.firstGenerator
//                val dft = DftMatrixPrime(root.primeSubroot(Random.nextUInt(root.order.factors.size.toUInt())))    //too many variation here
                val dft = DftMatrixPrime(root.primeSubroot(root.order.factors.size.toUInt() - 1u))  //very large prime
                val x = primeField.randomModularUIntMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }*/

}