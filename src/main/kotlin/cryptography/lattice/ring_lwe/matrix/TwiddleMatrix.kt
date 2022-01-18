package cryptography.lattice.ring_lwe.matrix

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.Ring
import math.coding.LadderIndex
import math.martix.AbstractDiagonalMatrix
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/17 16:28
 *
 * r^(b0*b1) = 1
 */
class TwiddleMatrix<A>(ring: Ring<A>, b0: BigInteger, b1: BigInteger, val rootOfUnity: A) : AbstractDiagonalMatrix<A>(ring, (b0 * b1).uintValue(true)) {

    val ladderIndex = LadderIndex(listOf(b0, b1))

    override fun vectorElementAt(index: UInt): A {
        require(index < rows)
        val a = ladderIndex.decode(index.toBigInteger())
        return ring.powerM(rootOfUnity, a[0] * a[1])
    }

    override fun vectorElementAtUnsafe(index: UInt): A {
        val a = ladderIndex.decode(index.toBigInteger())
        return ring.powerM(rootOfUnity, a[0] * a[1])
    }


}