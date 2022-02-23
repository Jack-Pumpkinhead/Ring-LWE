package math.coding

import math.coding.iterator.MultiIndexIteratorGray
import math.coding.iterator.MultiIndexIteratorLadder
import math.integer.uint.modInverse
import math.integer.uint.modMinusUnsafe
import math.integer.uint.modPlusUnsafe
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/24 13:23
 *
 * require bound to be pair-wise coprime and >1
 */
class ChineseRemainderIndex(bounds: List<UInt>, indexBound: UInt) : BoundedMultiIndex(bounds, indexBound) {

    init {
        require(bounds.isNotEmpty())
        lazyAssert2 {
            for (b in bounds) {
                assert(b >= 2u)
            }
        }
    }

    val indexBase: List<UInt> = List(bounds.size) { i ->
        val complement = indexBound / bounds[i]
        complement * complement.modInverse(bounds[i])
    }

    override fun encode(indices: List<UInt>): UInt {
        require(indices.size == indexBase.size)
        var sum = 0u
        for (i in indexBase.indices) {
            val product = indices[i].mod(bounds[i]).toULong() * indexBase[i]
            sum = (sum + product.mod(indexBound)).mod(indexBound)
        }
        return sum
    }

    override fun decode(index: UInt): List<UInt> = List(bounds.size) { i -> decodeAt(index, i) }

    override fun decodeAt(index: UInt, i: Int): UInt = index.mod(bounds[i])

    override fun firstIndex(): UInt = 0u

    override fun cyclicIncreaseAt(index: UInt, i: Int) = (index + indexBase[i]).mod(indexBound)
    override fun cyclicIncreaseToZeroAt(index: UInt, i: Int) = (index + indexBase[i]).mod(indexBound)
    override fun cyclicDecreaseAt(index: UInt, i: Int) = modMinusUnsafe(index, indexBase[i], indexBound)
    override fun increaseAt(index: UInt, i: Int): UInt = (index + indexBase[i]).mod(indexBound)
    override fun decreaseAt(index: UInt, i: Int): UInt = modMinusUnsafe(index, indexBase[i], indexBound)


    override fun iterator(): MultiIndexIteratorLadder<UInt> = object : MultiIndexIteratorLadder<UInt>(bounds, indexBound) {
        var encode = 0u
        override fun returnCode(): UInt = encode

        override fun atIncrease(i: Int) {
            encode = (encode + indexBase[i]).mod(indexBound)
        }

        override fun atCyclicIncreaseToZero(i: Int) {
            encode = (encode + indexBase[i]).mod(indexBound)
        }
    }

    override fun iteratorGray(): MultiIndexIteratorGray<UInt> = object : MultiIndexIteratorGray<UInt>(bounds, indexBound) {

        var encode = 0u
        override fun returnCode(): UInt = encode

        override fun atIncrease(i: Int) {
            encode = modPlusUnsafe(encode, indexBase[i], indexBound)
        }

        override fun atDecrease(i: Int) {
            encode = modMinusUnsafe(encode, indexBase[i], indexBound)
        }
    }
}