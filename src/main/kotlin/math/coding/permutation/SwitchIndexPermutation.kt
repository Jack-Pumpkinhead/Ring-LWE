package math.coding.permutation

import math.coding.BoundedMultiIndex
import math.coding.iterator.MultiIndexIteratorGray
import math.coding.iterator.MultiIndexIteratorLadder
import util.stdlib.lazyAssert

/**
 * Created by CowardlyLion at 2022/1/24 13:48
 */
class SwitchIndexPermutation(val fromIndex: BoundedMultiIndex, val toIndex: BoundedMultiIndex) : Permutation(fromIndex.indexBound) {

    init {
        lazyAssert { fromIndex.bounds == toIndex.bounds }
    }

    override fun invoke(x: UInt): UInt = toIndex.encode(fromIndex.decode(x))

    override fun inv(y: UInt): UInt = fromIndex.encode(toIndex.decode(y))

    override val inverse: Permutation
        get() = SwitchIndexPermutation(toIndex, fromIndex)

    //    override fun iterator(): Iterator<PermutationPair> = iteratorNormal()
    override fun iterator(): Iterator<PermutationPair> = iteratorGray()

    fun iteratorNormal(): MultiIndexIteratorLadder<PermutationPair> = object : MultiIndexIteratorLadder<PermutationPair>(fromIndex.bounds, fromIndex.indexBound) {

        var fromEncode = fromIndex.firstIndex()
        var toEncode = toIndex.firstIndex()

        override fun returnCode(): PermutationPair = PermutationPair(fromEncode, toEncode)

        override fun atIncrease(i: Int) {
            fromEncode = fromIndex.increaseAt(fromEncode, i)
            toEncode = toIndex.increaseAt(toEncode, i)
        }

        override fun atCyclicIncreaseToZero(i: Int) {
            fromEncode = fromIndex.cyclicIncreaseToZeroAt(fromEncode, i)
            toEncode = toIndex.cyclicIncreaseToZeroAt(toEncode, i)
        }
    }

    fun iteratorGray(): MultiIndexIteratorGray<PermutationPair> = object : MultiIndexIteratorGray<PermutationPair>(fromIndex.bounds, fromIndex.indexBound) {

        var fromEncode = fromIndex.firstIndex()
        var toEncode = toIndex.firstIndex()

        override fun returnCode(): PermutationPair = PermutationPair(fromEncode, toEncode)

        override fun atIncrease(i: Int) {
            fromEncode = fromIndex.increaseAt(fromEncode, i)
            toEncode = toIndex.increaseAt(toEncode, i)
        }

        override fun atDecrease(i: Int) {
            fromEncode = fromIndex.decreaseAt(fromEncode, i)
            toEncode = toIndex.decreaseAt(toEncode, i)
        }
    }


}