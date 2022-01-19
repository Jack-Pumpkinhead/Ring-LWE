package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import com.ionspin.kotlin.bignum.integer.toBigInteger
import cryptography.lattice.ring_lwe.coding.LadderSwitcher
import cryptography.lattice.ring_lwe.matrix.TwiddleMatrix
import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.martix.FormalProduct
import math.martix.permutationMatrix
import math.martix.whiskered
import math.operation.powers

/**
 * Created by CowardlyLion at 2022/1/16 16:28
 */
@Deprecated("to removed")
class DiscreteFourierTransformPrimePowerCache<A>(val ring: Ring<A>, val prime: PrimeData, val root: PrimePowerRootCalculator<A>) {


    private val matrixCache = mutableListOf<AbstractMatrix<A>>(DiscreteFourierTransformPrimeOld(ring, prime, ring.powers(root.primitiveRoot(prime.prime, 1u, prime.prime), 0u until prime.prime)))

    val p = prime.prime.toBigInteger()

    private var power = 1u
    private var primePower = p

    /**
     * need a root of unity of order [primePower]*[p]
     */
    private fun computeNext() {
        val nextPower = power + 1u
        val nextPrimePower = primePower * p
        matrixCache += FormalProduct(
            ring, listOf(
                ring.permutationMatrix(LadderSwitcher(primePower, p)),
                ring.whiskered(p.uintValue(), matrixCache.last(), 1u),
                TwiddleMatrix(ring, p, primePower, root.primitiveRoot(prime.prime, nextPower, nextPrimePower.uintValue(true))),
                ring.whiskered(1u, matrixCache[0], primePower.uintValue())
            )
        )
        primePower = nextPrimePower
        power = nextPower
    }

    fun primePowerDFT(power: UInt): AbstractMatrix<A> {
        require(power.toInt() > 0)
        while (power.toInt() > matrixCache.size) {
            computeNext()
        }
        return matrixCache[power.toInt() - 1]
    }


}