package math.coding

import math.integer.operation.modMinusUnsafe
import util.stdlib.lazyAssert2
import util.stdlib.runningFoldRight

/**
 * Created by CowardlyLion at 2022/1/22 23:28
 */
class LadderIndex(bounds: List<UInt>, indexBound: UInt) : BoundedMultiIndex(bounds, indexBound) {

    init {
        require(bounds.isNotEmpty())
        lazyAssert2 {
            for (b in bounds) {
                assert(b > 0u)
            }
        }
    }

    val indexBase: List<UInt> = bounds.runningFoldRight(1u) { acc, b -> acc * b }.drop(1)

    override fun encode(indices: List<UInt>): UInt {
        require(indices.size == indexBase.size)
        var sum = 0u
        for (i in indexBase.indices) {
            sum += indexBase[i] * indices[i].mod(bounds[i])
        }
        return sum
    }

    override fun decode(index: UInt): List<UInt> {
        val indices = mutableListOf<UInt>()
        var n = index.mod(indexBound)
        for (base in indexBase) {
            val div = n / base
            indices += div
            n -= div * base
        }
        return indices
    }

    override fun decodeAt(index: UInt, i: Int): UInt =
        if (i == 0) {
            index / indexBase[0]
        } else {
            val n = index.mod(indexBase[i - 1])
            n / indexBase[i]
        }

    override fun firstIndex(): UInt = 0u

    override fun cyclicIncreaseAt(index: UInt, i: Int): UInt =
        if (i == 0) {
            (index + indexBase[0]).mod(indexBound)
        } else {
            val n = index.mod(indexBase[i - 1])
            if (n / indexBase[i] == bounds[i] - 1u) {
                index + indexBase[i] - indexBase[i - 1]
            } else {
                index + indexBase[i]
            }
        }

    override fun cyclicDecreaseAt(index: UInt, i: Int): UInt =
        if (i == 0) {
            modMinusUnsafe(index, indexBase[0], indexBound)
        } else {
            if (index.mod(indexBase[i - 1]) < indexBase[i]) {
                index + indexBase[i - 1] - indexBase[i]
            } else {
                index - indexBase[i]
            }
        }

    override fun increaseAt(index: UInt, i: Int): UInt = index + indexBase[i]

    override fun decreaseAt(index: UInt, i: Int): UInt = index - indexBase[i]


    override fun iterator(): Iterator<UInt> = (0u until indexBound).iterator()

    override fun iteratorGray(): Iterator<UInt> = object : Iterator<UInt> {

        val code = MutableList(bounds.size) { 0u }
        var encode = 0u

        var first = true
        val reverseOrder = MutableList(bounds.size) { false }
        var i = bounds.size - 1

        private fun isAtEnd(i: Int): Boolean = if (reverseOrder[i]) code[i] == 0u else code[i] == bounds[i] - 1u

        override fun hasNext(): Boolean = first || i != bounds.size - 1 || !(0..i).all { isAtEnd(it) }

        override fun next(): UInt {
            if (first) {
                first = false
            } else {
                var shouldTryNext = tryNext()
                while (shouldTryNext) {
                    shouldTryNext = tryNext()
                }
            }
            return encode
        }

        private fun tryNext(): Boolean =
            if (reverseOrder[i]) {
                if (code[i] == 0u) {
                    reverseOrder[i] = !reverseOrder[i]
                    i--
                    true
                } else {
                    code[i]--
                    encode -= indexBase[i]
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
                    encode += indexBase[i]
                    i = bounds.size - 1
                    false
                }
            }
    }
}