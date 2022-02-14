package math.integer.uint.factored

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.integer.big_integer.RingBigInteger
import math.integer.uint.RingUInt
import math.integer.uint.coprimeElements
import math.operation.product
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/27 11:44
 *
 * represent a UInt with complete factorization is not a power of prime
 */
class ProperPrimePowerProductUInt(override val value: UInt, override val factors: List<UIntPPI>) : UIntPPPI {

    init {
        lazyAssert2 {
            assert(factors.size > 1)
            assert(value.toBigInteger() == RingBigInteger.product(factors.map { it.value.toBigInteger() }))
            for (i in factors.indices) {
                for (j in i + 1 until factors.size) {
                    assert(factors[i].prime != factors[j].prime)
                }
            }
        }
    }

    override val eulerTotient: UInt by lazy { RingUInt.product(factors.map { it.eulerTotient }) }

    override val radical: UInt by lazy { RingUInt.product(factors.map { it.prime }) }

    /**
     * result is always [UIntPPP] or [UIntPP] or [UIntP]
     */
    fun decreasePowerByOneAt(index: UInt): UIntPPPI {
        val factors1 = factors.toMutableList()
        val factor = factors[index.toInt()]
        when (factor.power) {
            1u   -> factors1.removeAt(index.toInt())
            2u   -> factors1[index.toInt()] = PrimeUInt(factor.prime)
            else -> factors1[index.toInt()] = ProperPrimePowerUInt(factor.value / factor.prime, factor.prime, factor.power - 1u)
        }
        return if (factors1.size > 1) {
            ProperPrimePowerProductUInt(this.value / factor.prime, factors1)
        } else {
            factors1[0]
        }
    }

    val coprimeNumbers: List<UInt> by lazy {
        value.coprimeElements()
    }

    override fun coprimeNumberAtUnsafe(i: UInt): UInt = coprimeNumbers[i.toInt()]

    override fun toString(): String {
        return "($value = $factors)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UIntPPPI) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}