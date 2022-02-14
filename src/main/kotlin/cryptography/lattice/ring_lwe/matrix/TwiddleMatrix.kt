package cryptography.lattice.ring_lwe.matrix

import cryptography.lattice.ring_lwe.ring.RootProperPrimePowerUInt
import math.abstract_structure.Ring
import math.coding.LadderIndex
import math.martix.AbstractDiagonalMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/17 16:28
 *
 * r^(b0*b1) = 1
 */
class TwiddleMatrix<A>(override val ring: Ring<A>, override val size: UInt, b0: UInt, b1: UInt, val root: RootProperPrimePowerUInt<A>) : AbstractDiagonalMatrix<A> {

    val ladderIndex = LadderIndex(listOf(b0, b1), size)

    init {
        lazyAssert2 {
            assert(size.toULong() == b0.toULong() * b1.toULong())
            assert(size == root.order.value)
        }
    }

    override fun vectorElementAtUnsafe(index: UInt): A {
        val a = ladderIndex.decode(index)
        return root.cachedPower(a[0] * a[1])  //use cached power
//        return ring.powerM(root, a[0] * a[1])
    }

//    override val inverse: AbstractSquareMatrix<A>
//        get() = TwiddleMatrix(ring, b0, b1, root.inverse)

    //TODO multiplication of TwiddleMatrix can also speed up by iteration of ladderIndex

}