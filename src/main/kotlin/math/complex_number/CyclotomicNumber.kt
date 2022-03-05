package math.complex_number

import math.abstract_structure.Monoid
import math.abstract_structure.monoid.MonoidElementCachePower
import math.integer.uint.*
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/3/3 23:32
 *
 * represent a complex number e^(2πi*(power/order))
 *
 * require [power] and [order] coprime, [power] < [order], [order] > 0
 */
class CyclotomicNumber(val power: UInt, override val order: UInt) : MonoidElementCachePower<ComplexNumber<Double>> {

    init {
        lazyAssert2 {
            assert(power < order)
            assert(isCoprime(order, power))
            assert(order > 0u)
        }
    }

    override val monoid: Monoid<ComplexNumber<Double>> get() = FieldComplexNumberDouble

    override val value: ComplexNumber<Double> = cyclotomicNumberUnsafe(power, order)

    override fun power(exponent: UInt): MonoidElementCachePower<ComplexNumber<Double>> {
        val exp = modTimes(power, exponent, order)
        val gcd = gcd(exp, order)
        return CyclotomicNumber(exp / gcd, order / gcd)
    }

    override var cacheComputed: Boolean = false

    private val powers: List<ComplexNumber<Double>> by lazy {
        cacheComputed = true
        if (order == 1u) listOf(monoid.one)
        else {
            val list = mutableListOf(monoid.one, value)
            for (i in 2u until order) {
                val exp = modTimes(power, i, order)
                val gcd = gcd(exp, order)
                list += cyclotomicNumberUnsafe(exp / gcd, order / gcd)
            }
            list
        }
    }

    override fun cachedPower(exponent: UInt): ComplexNumber<Double> = powers[exponent.toInt()]

    override val inverse: MonoidElementCachePower<ComplexNumber<Double>> by lazy {
        CyclotomicNumber(modUnaryMinusUnsafe(power, order), order)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CyclotomicNumber) return false

        if (power != other.power) return false
        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        var result = power.hashCode()
        result = 31 * result + order.hashCode()
        return result
    }

    override fun toString(): String {
        return "e^(2πi($power/$order)) = $value"
    }

}