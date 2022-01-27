package math.integer

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.instance.RingBigInteger
import math.operation.product
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/27 11:44
 */
data class FactorizationUInt(val value: UInt, val factors: List<FactorizationUIntPrimePower>) {

    init {
        lazyAssert2 {
            assert(value.toBigInteger() == RingBigInteger.product(factors.map { it.value.toBigInteger() }))
            for (i in factors.indices) {
                for (j in i + 1 until factors.size) {
                    assert(factors[i].value != factors[j].value)
                }
            }
        }
    }

    fun toPrimePower(): FactorizationUIntPrimePower {
        require(factors.size == 1)
        return factors[0]
    }

    fun decreasePowerByOneAt(index: UInt): FactorizationUInt {

        val factors1: MutableList<FactorizationUIntPrimePower> = factors.toMutableList()

        val factor = factors[index.toInt()]
        if (factor.power == 1u) {
            factors1.removeAt(index.toInt())
            require(factors1.isNotEmpty())
        } else {
            factors1[index.toInt()] = FactorizationUIntPrimePower(factor.value / factor.prime, factor.prime, factor.power - 1u)
        }
        return FactorizationUInt(this.value / factor.prime, factors1)
    }

    override fun toString(): String {
        return "($value = $factors)"
    }


}