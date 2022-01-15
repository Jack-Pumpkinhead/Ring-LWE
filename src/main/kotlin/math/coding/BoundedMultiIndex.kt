package math.coding

import com.ionspin.kotlin.bignum.integer.BigInteger

/**
 * Created by CowardlyLion at 2022/1/14 18:40
 */
abstract class BoundedMultiIndex(val bounds: List<BigInteger>) {

    abstract val indexBound: BigInteger   //to avoid extra computation, compute later.

    abstract fun encode(indices: List<BigInteger>): BigInteger
    abstract fun decode(index: BigInteger): List<BigInteger>

}