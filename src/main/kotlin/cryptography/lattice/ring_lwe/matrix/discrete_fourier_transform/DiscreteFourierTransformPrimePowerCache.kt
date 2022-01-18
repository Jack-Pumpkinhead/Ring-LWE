package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import com.ionspin.kotlin.bignum.integer.toBigInteger
import cryptography.lattice.ring_lwe.coding.LadderSwitcher
import cryptography.lattice.ring_lwe.matrix.TwiddleMatrix
import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.martix.FormalProduct
import math.martix.permutationMatrix
import math.martix.whiskered

/**
 * Created by CowardlyLion at 2022/1/16 16:28
 * [primitiveRootOfUnityCalculator] compute primitive root of unity of order n
 */
class DiscreteFourierTransformPrimePowerCache<A>(val ring: Ring<A>, prime: UInt, val primitiveRootOfUnityCalculator: (UInt) -> A) {

    val p = prime.toBigInteger()

    private val matrixCache = mutableListOf<AbstractMatrix<A>>(DiscreteFourierTransformPrime(ring, prime, primitiveRootOfUnityCalculator(prime)))
    private var primePower = p

    /**
     * need a root of unity of order [primePower]*[p]
     */
    private fun computeNext() {
        val order = primePower * p
        matrixCache += FormalProduct(
            ring, listOf(
                ring.permutationMatrix(LadderSwitcher(primePower, p)),
                ring.whiskered(p.uintValue(), matrixCache.last(), 1u),
                TwiddleMatrix(ring, p, primePower, primitiveRootOfUnityCalculator(order.uintValue(true))),
                ring.whiskered(1u, matrixCache[0], primePower.uintValue())
            )
        )
        primePower = order
    }

    /**
     * need a root of unity of order [p]^[power]
     */
    fun primePowerDFT(power: UInt): AbstractMatrix<A> {
        require(power.toInt() > 0)
        while (power.toInt() > matrixCache.size) {
            computeNext()
        }
        return matrixCache[power.toInt() - 1]
    }


}