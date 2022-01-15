package math.coding

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.modular.ModularBigInteger
import math.abstract_structure.instance.ringBigInteger
import math.operations.product

/**
 * Created by CowardlyLion at 2022/1/14 18:43
 *
 * require bound to be pair-wise coprime and >1
 */
class RuritanianIndex(bound: List<BigInteger>) : BoundedMultiIndex(bound) {

    init {
        require(bound.isNotEmpty())
        for (b in bound) {
            require(b >= 2u)
        }
    }

    override val indexBound: BigInteger = ringBigInteger.product(bound)


    val indexBase: List<ModularBigInteger> = List(bound.size) { (indexBound / bound[it]).toModularBigInteger(indexBound) }

    override fun encode(indices: List<BigInteger>): BigInteger {
        require(indexBase.size == indices.size)
        var sum = BigInteger.ZERO.toModularBigInteger(indexBound)
        for (i in indexBase.indices) {
            sum += indexBase[i] * indices[i].mod(bounds[i]).toModularBigInteger(indexBound)
        }
        return sum.residue
    }


    val indexBaseInv: List<BigInteger> = List(bound.size) { i -> indexBase[i].residue.modInverse(bound[i]) }

    override fun decode(index: BigInteger): List<BigInteger> = List(bounds.size) { i -> (index.mod(bounds[i]) * indexBaseInv[i]).mod(bounds[i]) }

}