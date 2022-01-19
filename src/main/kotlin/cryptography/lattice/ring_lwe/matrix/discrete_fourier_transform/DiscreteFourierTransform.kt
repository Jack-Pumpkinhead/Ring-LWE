package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.Ring
import math.coding.permCLInv
import math.coding.permLRInv
import math.integer.firstMultiplicativeGeneratorOfPrimeFieldUnsafe
import math.integer.primeFactorization
import math.martix.AbstractMatrix
import math.martix.FormalProduct
import math.martix.permutationMatrix
import math.martix.tensor.FormalKroneckerProduct

/**
 * Created by CowardlyLion at 2022/1/18 17:38
 *
 * DFT at arbitrary size is not used in Ring-LWE
 */
@Deprecated("to removed")
class DiscreteFourierTransform<A>(val ring: Ring<A>, val root: PrimePowerRootCalculator<A>) {

    private val primePowerCache = mutableMapOf<PrimeData, DiscreteFourierTransformPrimePowerCache<A>>()

    fun cacheAt(prime: PrimeData) = primePowerCache.computeIfAbsent(prime) {
        DiscreteFourierTransformPrimePowerCache(ring, prime, root)
    }

    suspend fun primeData(prime: UInt) = PrimeData(prime, firstMultiplicativeGeneratorOfPrimeFieldUnsafe(prime.toULong()).toUInt())

    suspend fun dft(order: UInt): AbstractMatrix<A> {
        val factorization = order.toULong().primeFactorization()
        if (factorization.size == 1) {
            val factor = factorization[0]
            return cacheAt(primeData(factor.prime.toUInt())).primePowerDFT(factor.power)
        } else {
            val factors = factorization.map { it.primePower.toBigInteger() }
            return FormalProduct(
                ring, listOf(
                    ring.permutationMatrix(permCLInv(factors)),
                    FormalKroneckerProduct(ring, factorization.map {
                        cacheAt(primeData(it.prime.toUInt())).primePowerDFT(it.power)
                    }),
                    ring.permutationMatrix(permLRInv(factors))
                )
            )
        }
    }

}