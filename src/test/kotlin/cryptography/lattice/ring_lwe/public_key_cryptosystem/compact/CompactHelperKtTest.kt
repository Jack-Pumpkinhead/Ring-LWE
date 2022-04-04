package cryptography.lattice.ring_lwe.public_key_cryptosystem.compact

import kotlinx.coroutines.runBlocking
import math.complex_number.maxRoundingError
import math.complex_number.roundToReal
import math.integer.uint.factored.primeFactorization
import org.junit.jupiter.api.Test
import util.stdlib.toString
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/4/4 20:30
 */
internal class CompactHelperKtTest {

    suspend fun samplingContinuousGaussianToDecodingBasisBase(range: UIntRange) {
        for (i in range) {
            val order = i.primeFactorization()
            println(order)
            println(order.eulerTotient.primeFactorization())
            val x = Random.samplingContinuousGaussianToDecodingBasis(order, 0.0, 1.0)

            val list = x.columnListAt(0u)
            list.map { it.roundToReal() }
            println(list.map { it.toString(20u) })
            println("maxError: $maxRoundingError")
        }
    }

    @Test
    fun samplingContinuousGaussianToDecodingBasis() = runBlocking {
        samplingContinuousGaussianToDecodingBasisBase(3u..1000u)
    }

    @Test
    fun samplingContinuousGaussianToDecodingBasisLarge() = runBlocking {
        samplingContinuousGaussianToDecodingBasisBase(12289u..12289u)
    }


}