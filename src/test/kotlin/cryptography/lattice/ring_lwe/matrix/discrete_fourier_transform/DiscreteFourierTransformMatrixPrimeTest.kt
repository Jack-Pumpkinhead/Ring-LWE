package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.matrix.RootDataUInt
import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.FieldModularUInt
import math.integer.firstMultiplicativeGeneratorOfPrimeFieldUnsafe
import math.integer.modular.ModularUInt
import math.integer.primeFactorization
import math.integer.primeOf
import math.random.randomModularUIntMatrix
import math.statistic.RepeatTaskStatistic
import math.timing.TwoMatrix
import math.timing.TwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/19 23:13
 */
internal class DiscreteFourierTransformMatrixPrimeTest {

    //total 23s
    @Test
    fun primeField() {
        runBlocking {
            val statistic = RepeatTaskStatistic(TwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1u..700u) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val order = primeField.prime - 1u
                val root = firstMultiplicativeGeneratorOfPrimeFieldUnsafe(primeField.prime)
                val rootData = RootDataUInt(primeField, ModularUInt(primeField.prime, root), order.primeFactorization())
                val dft = DiscreteFourierTransformMatrixPrime(rootData.primeSubroot(Random.nextUInt(rootData.order.factors.size.toUInt())))
                val x = primeField.randomModularUIntMatrix(dft.columns..dft.columns, 1u..3u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    //total 42s
    @Test
    fun largePrimeField() {
        runBlocking {
            val statistic = RepeatTaskStatistic(TwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in 1500u..1550u) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val order = primeField.prime - 1u
                val root = firstMultiplicativeGeneratorOfPrimeFieldUnsafe(primeField.prime)
                val rootData = RootDataUInt(primeField, ModularUInt(primeField.prime, root), order.primeFactorization())
                val dft = DiscreteFourierTransformMatrixPrime(rootData.primeSubroot(Random.nextUInt(rootData.order.factors.size.toUInt())))
                val x = primeField.randomModularUIntMatrix(dft.columns..dft.columns, 1u..3u)
                statistic.go(TwoMatrix(dft, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

}