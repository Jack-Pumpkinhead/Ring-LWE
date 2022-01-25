package cryptography.lattice.ring_lwe.coding

import math.coding.LadderIndex
import math.coding.permutation.Permutation

/**
 * Created by CowardlyLion at 2022/1/24 13:44
 *
 * from LadderIndex (a0, a1) with bounds {b0, b1} to LadderIndex (a1, a0) with bounds {b1, b0}
 */
class LadderSwitcher(val b0: UInt, val b1: UInt) : Permutation(b0 * b1) {

    val fromIndex = LadderIndex(listOf(b0, b1), super.size)
    val toIndex = LadderIndex(listOf(b1, b0), super.size)

    override fun invoke(x: UInt): UInt {
        val a = fromIndex.decode(x)
        return toIndex.encode(listOf(a[1], a[0]))
    }

    override fun inv(y: UInt): UInt {
        val a = toIndex.decode(y)
        return fromIndex.encode(listOf(a[1], a[0]))
    }

    override fun iterator(): Iterator<PermutationPair> = object : Iterator<PermutationPair> {

        var index = 0u
        val fromCode = mutableListOf(0u, 0u)

        override fun hasNext(): Boolean = index != this@LadderSwitcher.size

        override fun next(): PermutationPair {
            if (index != 0u) {
                if (fromCode[1] == b1 - 1u) {
                    fromCode[1] = 0u
                    fromCode[0]++
                } else {
                    fromCode[1]++
                }
            }
            index++
            return PermutationPair(fromIndex.encode(fromCode), toIndex.encode(listOf(fromCode[1], fromCode[0])))
        }
    }

}