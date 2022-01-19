package math.coding

import com.ionspin.kotlin.bignum.integer.BigInteger
import util.stdlib.runningFoldRight

/**
 * Created by CowardlyLion at 2022/1/9 22:17
 *
 * give a bijection between (multi-index {a_i}_i with bound {b_i}_i) to (0 until Π_i b_i)
 */
class LadderIndex(bounds: List<BigInteger>) : BoundedMultiIndex(bounds) {

    val indexBase: List<BigInteger>
    override val indexBound: BigInteger

    init {
        require(bounds.isNotEmpty())
        for (b in bounds) {
            require(b >= BigInteger.ONE)
        }

        val accumulation = bounds.runningFoldRight(BigInteger.ONE) { acc, b -> acc * b }
        indexBound = accumulation[0]
        indexBase = accumulation.drop(1)
    }


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