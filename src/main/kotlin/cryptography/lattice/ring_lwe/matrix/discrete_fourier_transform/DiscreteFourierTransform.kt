package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.Ring
import math.coding.permCLInv
import math.coding.permLRInv
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
class DiscreteFourierTransform<A>(val ring: Ring<A>, val primitiveRootOfUnityCalculator: (UInt) -> A) {

    private val primePowerCache = mutableMapOf<UInt, DiscreteFourierTransformPrimePowerCache<A>>()
    fun cacheAt(prime: UInt) = primePowerCache.computeIfAbsent(prime) { DiscreteFourierTransformPrimePowerCache(ring, prime, primitiveRootOfUnityCalculator) }

    suspend fun dft(order: UInt): AbstractMatrix<A> {
        val factorization = order.toULong().primeFactorization()
        val factors = factorization.map { it.primePower.toBigInteger() }
        return FormalProduct(
            ring, listOf(
                ring.permutationMatrix(permCLInv(factors)),
                FormalKroneckerProduct(ring, factorization.map { cacheAt(it.prime.toUInt()).primePowerDFT(it.power) }),
                ring.permutationMatrix(permLRInv(factors))
            )
        )
    }

}