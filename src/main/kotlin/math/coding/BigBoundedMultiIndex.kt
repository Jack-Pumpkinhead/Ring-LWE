package math.coding

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.abstract_structure.instance.RingBigInteger
import math.operation.product
import util.stdlib.lazyAssert

/**
 * Created by CowardlyLion at 2022/1/14 18:40
 *
 * give a bijection between (multi-index {a_i}_i with bound [bounds]) to (0 until Π_i b_i = [indexBound])
 *
 * require [indexBound] equals product of [bounds] to avoid duplicate computation
 */
abstract class BigBoundedMultiIndex(val bounds: List<BigInteger>, val indexBound: BigInteger) {

    init {
        lazyAssert { RingBigInteger.product(bounds) == indexBound }
    }

    abstract fun encode(indices: List<BigInteger>): BigInteger
    abstract fun decode(index: BigInteger): List<BigInteger>

}