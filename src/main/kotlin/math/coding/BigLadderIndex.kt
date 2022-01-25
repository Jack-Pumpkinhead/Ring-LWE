package math.coding

import com.ionspin.kotlin.bignum.integer.BigInteger
import util.stdlib.lazyAssert2
import util.stdlib.runningFoldRight

/**
 * Created by CowardlyLion at 2022/1/9 22:17
 */
class BigLadderIndex(bounds: List<BigInteger>, indexBound: BigInteger) : BigBoundedMultiIndex(bounds, indexBound) {

    init {
        require(bounds.isNotEmpty())
        lazyAssert2 {
            for (b in bounds) {
                assert(b >= BigInteger.ONE)
            }
        }
    }

    val indexBase: List<BigInteger> = bounds.runningFoldRight(BigInteger.ONE) { acc, b -> acc * b }.drop(1)

    override fun encode(indices: List<BigInteger>): BigInteger {
        require(indices.size == indexBase.size)
        var sum = BigInteger.ZERO
        for (i in indexBase.indices) {
            sum += indexBase[i] * indices[i].mod(bounds[i])
        }
        return sum
    }

    override fun decode(index: BigInteger): List<BigInteger> {
        val indices = mutableListOf<BigInteger>()
        var n = index.mod(indexBound)
        for (base in indexBase) {
            val div = n / base
            indices += div
            n -= div * base
        }
        return indices
    }

}