package math.complex_number

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/26 14:01
 */
class RealComplexNumber<A>(ring: Ring<A>, real: A) : ComplexNumber<A>(ring, real, ring.zero) {

    override fun plus(y: ComplexNumber<A>): ComplexNumber<A> = when (y) {
        is RealComplexNumber      -> RealComplexNumber(ring, ring.add(this.real, y.real))
        is ImaginaryComplexNumber -> ComplexNumber(ring, this.real, y.imaginary)
        else                      -> ComplexNumber(ring, ring.add(this.real, y.real), y.imaginary)
    }

    override fun unaryMinus(): ComplexNumber<A> = RealComplexNumber(ring, ring.negate(real))

    override fun minus(y: ComplexNumber<A>): ComplexNumber<A> = when (y) {
        is RealComplexNumber      -> RealComplexNumber(ring, ring.subtract(this.real, y.real))
        is ImaginaryComplexNumber -> ComplexNumber(ring, this.real, ring.negate(y.imaginary))
        else                      -> ComplexNumber(ring, ring.subtract(this.real, y.real), ring.negate(y.imaginary))
    }

    override fun times(y: ComplexNumber<A>): ComplexNumber<A> = when (y) {
        is RealComplexNumber      -> RealComplexNumber(ring, ring.multiply(this.real, y.real))
        is ImaginaryComplexNumber -> ImaginaryComplexNumber(ring, ring.multiply(this.real, y.imaginary))
        else                      -> ComplexNumber(ring, ring.multiply(this.real, y.real), ring.multiply(this.real, y.imaginary))
    }

    override fun conjugate(): ComplexNumber<A> = this

    override fun lengthSquared(): A = ring.multiply(real, real)

    override fun hasInverse(): Boolean = ring.hasInverse(real)

    override fun inverse(): ComplexNumber<A> = RealComplexNumber(ring, ring.inverse(real))
}