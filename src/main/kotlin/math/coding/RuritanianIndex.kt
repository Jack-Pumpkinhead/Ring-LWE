package math.coding

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.modular.ModularBigInteger
import math.abstract_structure.instance.ringBigInteger
import math.operation.product

/**
 * Created by CowardlyLion at 2022/1/14 18:43
 *
 * require bound to be pair-wise coprime and >1
 */
class RuritanianIndex(bounds: List<BigInteger>) : BoundedMultiIndex(bounds) {

    init {
        require(bounds.isNotEmpty())
        for (b in bounds) {
            require(b >= 2u)
        }
    }

    override val indexBound: BigInteger = ringBigInteger.product(bounds)


    val indexBase: List<ModularBigInteger> = List(bounds.size) { (indexBound / bounds[it]).toModularBigInteger(indexBound) }

    override fun encode(indices: List<BigInteger>): BigInteger {
        require(indexBase.size == indices.size)
        var sum = BigInteger.ZERO.toModularBigInteger(indexBound)
        for (i in indexBase.indices) {
            sum += indexBase[i] * indices[i].mod(bounds[i]).toModularBigInteger(indexBound)
        }
        return sum.residue
    }


    val indexBaseInv: List<BigInteger> = List(bounds.size) { i -> indexBase[i].residue.modInverse(bounds[i]) }

    override fun decode(index: BigInteger): List<BigInteger> = List(bounds.size) { i -> (index.mod(bounds[i]) * indexBaseInv[i]).mod(bounds[i]) }

}