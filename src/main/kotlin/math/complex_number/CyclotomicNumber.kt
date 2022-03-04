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

    override val cacheComputed: Boolean = true

    override fun cachedPower(exponent: UInt): ComplexNumber<Double> {
        val exp = modTimes(power, exponent, order)
        val gcd = gcd(exp, order)
        return cyclotomicNumberUnsafe(exp / gcd, order / gcd)
    }

    override val inverse: MonoidElementCachePower<ComplexNumber<Double>> by lazy {
        CyclotomicNumber(modUnaryMinusUnsafe(power, order), order)
    }

}