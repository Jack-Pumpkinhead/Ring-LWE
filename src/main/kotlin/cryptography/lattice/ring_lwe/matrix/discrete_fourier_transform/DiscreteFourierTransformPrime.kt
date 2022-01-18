package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/16 15:04,
 * [r] is primitive root of unity of order [prime],
 * [r]^[prime] = 1
 */
class DiscreteFourierTransformPrime<A>(ring: Ring<A>, val prime: UInt, val r: A) : AbstractMatrix<A>(ring, prime, prime) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        return ring.powerM(r, row.toBigInteger() * column.toBigInteger())
    }

}