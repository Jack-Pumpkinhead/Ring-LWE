package cryptography.lattice.ring_lwe.matrix

import cryptography.lattice.ring_lwe.ring.RootProperPrimePowerUInt
import math.abstract_structure.Ring
import math.coding.LadderIndex
import math.martix.AbstractDiagonalMatrix

/**
 * Created by CowardlyLion at 2022/2/12 17:22
 *
 * as submatrix of [TwiddleMatrix] of size (b0+1) x b1 and remove first b1 rows/columns
 */
class ReducedTwiddleMatrix<A>(override val ring: Ring<A>, override val size: UInt, b0: UInt, b1: UInt, val root: RootProperPrimePowerUInt<A>) : AbstractDiagonalMatrix<A> {

    val ladderIndex = LadderIndex(listOf(b0, b1), size)

    override fun vectorElementAtUnsafe(index: UInt): A {
        val a = ladderIndex.decode(index)
        return root.cachedPower((a[0] + 1u) * a[1])  //use cached power
//        return ring.powerM(root, (a[0] + 1u) * a[1])
    }

    //TODO multiplication of ReducedTwiddleMatrix can also speed up by iteration of ladderIndex


}