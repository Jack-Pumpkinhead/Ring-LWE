package cryptography.lattice.ring_lwe.coding

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.coding.LadderIndex
import math.coding.Permutation

/**
 * Created by CowardlyLion at 2022/1/17 16:22
 *
 * from index (a0, a1)_(b0, b1) to index (a1, a0)_(b1, b0)
 */
class LadderSwitcher(b0: BigInteger, b1: BigInteger) : Permutation(b0 * b1) {

    val fromIndex = LadderIndex(listOf(b0, b1))
    val toIndex = LadderIndex(listOf(b1, b0))

    override fun invoke(x: BigInteger): BigInteger {
        val a = fromIndex.decode(x)
        return toIndex.encode(listOf(a[1], a[0]))
    }

    override fun inv(y: BigInteger): BigInteger {
        val a = toIndex.decode(y)
        return fromIndex.encode(listOf(a[1], a[0]))
    }

}