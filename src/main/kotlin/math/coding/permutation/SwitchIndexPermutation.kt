package math.coding.permutation

import math.coding.BoundedMultiIndex
import util.stdlib.lazyAssert

/**
 * Created by CowardlyLion at 2022/1/24 13:48
 */
class SwitchIndexPermutation(val fromIndex: BoundedMultiIndex, val toIndex: BoundedMultiIndex) : Permutation(fromIndex.indexBound) {

    val bounds = fromIndex.bounds
    val indexBound = fromIndex.indexBound

    init {
        lazyAssert { fromIndex.bounds == toIndex.bounds }
    }

    override fun invoke(x: UInt): UInt = toIndex.encode(fromIndex.decode(x))

    override fun inv(y: UInt): UInt = fromIndex.encode(toIndex.decode(y))

//    override fun iterator(): Iterator<PermutationPair> = iteratorNormal()
    override fun iterator(): Iterator<PermutationPair> = iteratorGray()

    fun iteratorNormal(): Iterator<PermutationPair> = object : Iterator<PermutationPair> {

        val code = MutableList(bounds.size) { 0u }
        var fromEncode = fromIndex.firstIndex()
        var toEncode = toIndex.firstIndex()

        var first = true

        private fun isAtEnd(i: Int): Boolean = code[i] == bounds[i] - 1u

        override fun hasNext(): Boolean = first || !bounds.indices.all { isAtEnd(it) }

        override fun next(): PermutationPair {
            if (first) {
                first = false
            } else {
                var i = bounds.size - 1
                var end = isAtEnd(i)
                while (end) {
                    code[i] = 0u
                    fromEncode = fromIndex.cyclicIncreaseAt(fromEncode, i)
                    toEncode = toIndex.cyclicIncreaseAt(toEncode, i)
                    i--
                    end = isAtEnd(i)
                }
                code[i]++
                fromEncode = fromIndex.cyclicIncreaseAt(fromEncode, i)
                toEncode = toIndex.cyclicIncreaseAt(toEncode, i)
            }
            return PermutationPair(fromEncode, toEncode)
        }
    }

    fun iteratorGray(): Iterator<PermutationPair> = object : Iterator<PermutationPair> {

        val code = MutableList(bounds.size) { 0u }
        var fromEncode = fromIndex.firstIndex()
        var toEncode = toIndex.firstIndex()

        var first = true
        val reverseOrder = MutableList(bounds.size) { false }
        var i = bounds.size - 1


        private fun isAtEnd(i: Int): Boolean = if (reverseOrder[i]) code[i] == 0u else code[i] == bounds[i] - 1u

        override fun hasNext(): Boolean = first || i != bounds.size - 1 || !(0..i).all { isAtEnd(it) }

        override fun next(): PermutationPair {
            if (first) {
                first = false
            } else {
                var shouldTryNext = tryNext()
                while (shouldTryNext) {
                    shouldTryNext = tryNext()
                }
            }
            return PermutationPair(fromEncode, toEncode)
        }

        private fun tryNext(): Boolean =
            if (reverseOrder[i]) {
                if (code[i] == 0u) {
                    reverseOrder[i] = !reverseOrder[i]
                    i--
                    true
                } else {
                    code[i]--
                    fromEncode = fromIndex.decreaseAt(fromEncode, i)
                    toEncode = toIndex.decreaseAt(toEncode, i)
                    i = bounds.size - 1
                    false
                }
            } else {
                if (code[i] == bounds[i] - 1u) {
                    reverseOrder[i] = !reverseOrder[i]
                    i--
                    true
                } else {
                    code[i]++
                    fromEncode = fromIndex.increaseAt(fromEncode, i)
                    toEncode = toIndex.increaseAt(toEncode, i)
                    i = bounds.size - 1
                    false
                }
            }
    }


}