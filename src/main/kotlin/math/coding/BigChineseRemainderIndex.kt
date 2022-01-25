package math.coding

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.modular.ModularBigInteger
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/14 22:01
 *
 * require bound to be pair-wise coprime and >1
 */
class BigChineseRemainderIndex(bounds: List<BigInteger>, indexBound: BigInteger) : BigBoundedMultiIndex(bounds, indexBound) {

    init {
        require(bounds.isNotEmpty())
        lazyAssert2 {
            for (b in bounds) {
                assert(b >= 2u)
            }
        }
    }

    val indexBase: List<ModularBigInteger> = List(bounds.size) { i ->
        val complement = indexBound / bounds[i]
        (complement * complement.modInverse(bounds[i])).toModularBigInteger(indexBound)
    }

    override fun encode(indices: List<BigInteger>): BigInteger {
        require(indices.size == indexBase.size)
        var sum = BigInteger.ZERO.toModularBigInteger(indexBound)
        for (i in indexBase.indices) {
            sum += indexBase[i] * indices[i].mod(bounds[i]).toModularBigInteger(indexBound)
        }
        return sum.residue
    }

    override fun decode(index: BigInteger): List<BigInteger> = List(bounds.size) { i -> index.mod(bounds[i]) }

}