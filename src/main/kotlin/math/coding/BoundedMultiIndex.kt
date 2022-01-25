package math.coding

import math.abstract_structure.instance.ringUInt
import math.operation.product
import util.stdlib.lazyAssert

/**
 * Created by CowardlyLion at 2022/1/22 23:23
 *
 * give a bijection between (multi-index {a_i}_i with bound [bounds]) to (0 until Π_i b_i = [indexBound])
 *
 * require [indexBound] equals product of [bounds] to avoid duplicate computation
 *
 * require [indexBound] actually within [0, [Int.MAX_VALUE]]
 *
 * checking indexBound overflow is at construction's duty
 */
abstract class BoundedMultiIndex(val bounds: List<UInt>, val indexBound: UInt) : Iterable<UInt> {

    init {
        lazyAssert { ringUInt.product(bounds) == indexBound }
        lazyAssert { indexBound.toInt() > 0 }
    }

    abstract fun encode(indices: List<UInt>): UInt
    abstract fun decode(index: UInt): List<UInt>

    abstract fun decodeAt(index: UInt, i: Int): UInt

    open fun firstIndex(): UInt = encode(List(bounds.size) { 0u })

    /**
     * decode [index], cyclic increase multi-index at [i] by 1 then encode
     */
    abstract fun cyclicIncreaseAt(index: UInt, i: Int): UInt

    /**
     * assume code at [i] is maximal, then increase it to 0
     * decode [index], cyclic increase multi-index at [i] by 1 to 0 then encode
     */
    abstract fun cyclicIncreaseToZeroAt(index: UInt, i: Int): UInt

    /**
     * decode [index], cyclic decrease multi-index at [i] by 1 then encode
     */
    abstract fun cyclicDecreaseAt(index: UInt, i: Int): UInt

    /**
     * decode [index], increase multi-index at [i] by 1 then encode
     *
     * unsafe function, make sure no out-of-bounds occur
     */
    abstract fun increaseAt(index: UInt, i: Int): UInt

    /**
     * decode [index], decrease multi-index at [i] by 1 then encode
     *
     * unsafe function, make sure no out-of-bounds occur
     */
    abstract fun decreaseAt(index: UInt, i: Int): UInt

    /**
     * normal ordering
     */
    abstract override fun iterator(): Iterator<UInt>

    /**
     * mix-radix Gray code ordering
     */
    abstract fun iteratorGray(): Iterator<UInt>
}