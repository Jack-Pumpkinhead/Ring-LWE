package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete

import kotlinx.coroutines.runBlocking
import math.andPrint
import math.integer.primeFactorization1
import math.integer.primeOf
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/19 15:44
 */
internal class DiscreteFourierTransformPrimeFieldUIntKtTest {

    @Test
    fun primeField() {
        runBlocking {

            for (i in 1u..11u) {
                val mainPrime = primeOf(i)
                val factorization = primeFactorization1(mainPrime - 1uL)
                val dft = primeFieldDFTOld(mainPrime)
                println("-----------------------")
                for (factor in factorization) {
                    dft.dft(factor.prime.toUInt()).andPrint()
                }
                println("-----------------------")
//                val matrix = dft.dft(mainPrime.toUInt() - 1u).andPrint()

            }

        }
    }
}