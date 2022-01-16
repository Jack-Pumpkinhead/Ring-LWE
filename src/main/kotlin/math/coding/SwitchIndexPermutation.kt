package math.coding

import com.ionspin.kotlin.bignum.integer.BigInteger

/**
 * Created by CowardlyLion at 2022/1/16 20:31
 */
abstract class SwitchIndexPermutation(size: BigInteger) : Permutation(size) {

    abstract val fromIndex: BoundedMultiIndex
    abstract val toIndex: BoundedMultiIndex

    override fun invoke(x: BigInteger): BigInteger = toIndex.encode(fromIndex.decode(x))

    override fun inv(y: BigInteger): BigInteger = fromIndex.encode(toIndex.decode(y))

}