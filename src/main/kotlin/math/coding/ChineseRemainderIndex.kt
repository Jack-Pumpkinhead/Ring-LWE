package math.coding

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.modular.ModularBigInteger
import math.abstract_structure.instance.ringBigInteger
import math.operation.product

/**
 * Created by CowardlyLion at 2022/1/14 22:01
 *
 * require bound to be pair-wise coprime and >1
 */
class ChineseRemainderIndex(bounds: List<BigInteger>) : BoundedMultiIndex(bounds) {

    init {
        require(bounds.isNotEmpty())
        for (b in bounds) {
            require(b >= 2u)
        }
    }

    override val indexBound: BigInteger = ringBigInteger.product(bounds)


    val indexBase: List<ModularBigInteger> = List(bounds.size) { i ->
        val complement = indexBound / bounds[i]
        (complement * complement.modInverse(bounds[i])).toModularBigInteger(indexBound)
    }

    override fun encode(indices: List<BigInteger>): BigInteger {
        require(indexBase.size == indices.size)
        var sum = BigInteger.ZERO.toModularBigInteger(indexBound)
        for (i in indexBase.indices) {
            sum += indexBase[i] * indices[i].mod(bounds[i]).toModularBigInteger(indexBound)
        }
        return sum.residue
    }

    override fun decode(index: BigInteger): List<BigInteger> = List(bounds.size) { i -> index.mod(bounds[i]) }

}