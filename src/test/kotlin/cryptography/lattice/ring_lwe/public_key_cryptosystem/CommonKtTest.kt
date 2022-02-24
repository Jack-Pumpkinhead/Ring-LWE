package cryptography.lattice.ring_lwe.public_key_cryptosystem

import kotlinx.coroutines.runBlocking
import math.complex_number.maxRoundingError
import math.complex_number.roundToReal
import math.integer.uint.factored.UIntPPI
import math.integer.uint.factored.primeFactorization
import org.junit.jupiter.api.Test
import util.stdlib.toString
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/2/24 22:53
 */
internal class CommonKtTest {

    @Test
    fun samplingContinuousGaussianToDecodingBasis() {
        runBlocking {
            for (i in 3u..1000u) {
                val order = i.primeFactorization()
                if (order is UIntPPI) {
                    println(order.eulerTotient.primeFactorization())
                    val x = Random.samplingContinuousGaussianToDecodingBasis(order, 0.0, 1.0)
                    val list = x.columnListAt(0u)
                    list.map { it.roundToReal() }
                    println(list.map { it.toString(20u) })
                    println("maxError: $maxRoundingError")
                }
            }
        }

    }
}