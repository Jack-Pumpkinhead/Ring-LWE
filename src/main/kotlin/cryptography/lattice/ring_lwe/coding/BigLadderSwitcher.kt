package cryptography.lattice.ring_lwe.coding

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.coding.BigLadderIndex
import math.coding.permutation.BigPermutation

/**
 * Created by CowardlyLion at 2022/1/17 16:22
 *
 * from LadderIndex (a0, a1) with bounds {b0, b1} to LadderIndex (a1, a0) with bounds {b1, b0}
 */
class BigLadderSwitcher(b0: BigInteger, b1: BigInteger) : BigPermutation(b0 * b1) {

    val fromIndex = BigLadderIndex(listOf(b0, b1), super.size)
    val toIndex = BigLadderIndex(listOf(b1, b0), super.size)

    override fun invoke(x: BigInteger): BigInteger {
        val a = fromIndex.decode(x)
        return toIndex.encode(listOf(a[1], a[0]))
    }

    override fun inv(y: BigInteger): BigInteger {
        val a = toIndex.decode(y)
        return fromIndex.encode(listOf(a[1], a[0]))
    }

}