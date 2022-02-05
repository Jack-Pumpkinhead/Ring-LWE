package cryptography.lattice.ring_lwe.matrix

import math.abstract_structure.Ring
import math.coding.LadderIndex
import math.martix.AbstractDiagonalMatrix
import math.abstract_structure.algorithm.powerM
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/17 16:28
 *
 * r^(b0*b1) = 1
 */
class TwiddleMatrix<A>(override val ring: Ring<A>, b0: UInt, b1: UInt, val root: A) : AbstractDiagonalMatrix<A> {

    override val size: UInt = b0 * b1

    val ladderIndex = LadderIndex(listOf(b0, b1), size)

    init {
        lazyAssert2 {
            val size = b0.toULong() * b1.toULong()
            assert(size <= UInt.MAX_VALUE)
        }
    }

    override fun vectorElementAt(index: UInt): A {
        require(index < rows)
        val a = ladderIndex.decode(index)
        return ring.powerM(root, a[0] * a[1])
    }

    override fun vectorElementAtUnsafe(index: UInt): A {
        val a = ladderIndex.decode(index)
        return ring.powerM(root, a[0] * a[1])
    }


}