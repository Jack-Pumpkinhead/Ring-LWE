package math.coding

import math.integer.modInverse
import math.integer.operation.modMinusUnsafe
import math.integer.operation.modPlusUnsafe
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/23 20:31
 *
 * require bound to be pair-wise coprime and >1
 */
class RuritanianIndex(bounds: List<UInt>, indexBound: UInt) : BoundedMultiIndex(bounds, indexBound) {

    init {
        require(bounds.isNotEmpty())
        lazyAssert2 {
            for (b in bounds) {
                assert(b >= 2u)
            }
        }
    }

    val indexBase: List<UInt> = List(bounds.size) { indexBound / bounds[it] }

    override fun encode(indices: List<UInt>): UInt {
        require(indices.size == indexBase.size)
        var sum = 0u
        for (i in indexBase.indices) {
            val product = indices[i].mod(bounds[i]) * indexBase[i]  //product is within indexBound
            sum = (sum + product).mod(indexBound)   //addition never overflow if actual value of indexBound is positive Int
        }
        return sum
    }


    val indexBaseInv: List<ULong> = List(bounds.size) { i -> indexBase[i].modInverse(bounds[i]).toULong() }

    override fun decode(index: UInt): List<UInt> = List(bounds.size) { i -> decodeAt(index, i) }

    //        (index.mod(bounds[i]) * indexBaseInv[i]).mod(bounds[i])   //may overflow
    override fun decodeAt(index: UInt, i: Int): UInt = (index.mod(bounds[i]).toULong() * indexBaseInv[i]).mod(bounds[i])

    override fun firstIndex(): UInt = 0u

    override fun cyclicIncreaseAt(index: UInt, i: Int) = (index + indexBase[i]).mod(indexBound)
    override fun cyclicDecreaseAt(index: UInt, i: Int) = modMinusUnsafe(index, indexBase[i], indexBound)
    override fun increaseAt(index: UInt, i: Int): UInt = (index + indexBase[i]).mod(indexBound)
    override fun decreaseAt(index: UInt, i: Int): UInt = modMinusUnsafe(index, indexBase[i], indexBound)

    override fun iterator(): Iterator<UInt> = object : Iterator<UInt> {

        var first = true
        val code = MutableList(bounds.size) { 0u }
        var encode = 0u

        private fun isAtEnd(i: Int): Boolean = code[i] == bounds[i] - 1u

        override fun hasNext(): Boolean = first || !bounds.indices.all { isAtEnd(it) }

        override fun next(): UInt {
            if (first) {
                first = false
            } else {
                var i = bounds.size - 1
                var end = isAtEnd(i)
                while (end) {
                    code[i] = 0u
                    encode = (encode + indexBase[i]).mod(indexBound)
                    i--
                    end = isAtEnd(i)
                }
                code[i]++
                encode = (encode + indexBase[i]).mod(indexBound)
            }
            return encode
        }
    }

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
                    encode = modMinusUnsafe(encode, indexBase[i], indexBound)
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
                    encode = modPlusUnsafe(encode, indexBase[i], indexBound)
                    i = bounds.size - 1
                    false
                }
            }
    }
}