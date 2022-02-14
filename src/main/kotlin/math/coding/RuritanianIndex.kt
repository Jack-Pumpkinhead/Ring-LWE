package math.coding

import math.coding.iterator.GrayCodeIterator
import math.coding.iterator.MultiIndexIterator
import math.integer.uint.modInverse
import math.integer.uint.modMinusUnsafe
import math.integer.uint.modPlusUnsafe
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
    override fun cyclicIncreaseToZeroAt(index: UInt, i: Int) = (index + indexBase[i]).mod(indexBound)
    override fun cyclicDecreaseAt(index: UInt, i: Int) = modMinusUnsafe(index, indexBase[i], indexBound)
    override fun increaseAt(index: UInt, i: Int): UInt = (index + indexBase[i]).mod(indexBound)
    override fun decreaseAt(index: UInt, i: Int): UInt = modMinusUnsafe(index, indexBase[i], indexBound)

    override fun iterator(): MultiIndexIterator<UInt> = object : MultiIndexIterator<UInt>(bounds, indexBound) {

        var encode = 0u
        override fun returnCode(): UInt = encode

        override fun atIncrease(i: Int) {
            encode = (encode + indexBase[i]).mod(indexBound)
        }

        override fun atCyclicIncreaseToZero(i: Int) {
            encode = (encode + indexBase[i]).mod(indexBound)
        }
    }

    override fun iteratorGray(): GrayCodeIterator<UInt> = object : GrayCodeIterator<UInt>(bounds, indexBound) {

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