package math.coding.permutation

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.coding.BigBoundedMultiIndex
import util.stdlib.lazyAssert

/**
 * Created by CowardlyLion at 2022/1/16 20:31
 */
class BigSwitchIndexPermutation(val fromIndex: BigBoundedMultiIndex, val toIndex: BigBoundedMultiIndex) : BigPermutation(fromIndex.indexBound) {

    init {
        lazyAssert { fromIndex.bounds == toIndex.bounds }
    }

    override fun invoke(x: BigInteger): BigInteger = toIndex.encode(fromIndex.decode(x))

    override fun inv(y: BigInteger): BigInteger = fromIndex.encode(toIndex.decode(y))

}