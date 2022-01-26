package math.complex_number

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/26 14:04
 */
class ImaginaryComplexNumber<A>(ring: Ring<A>, imaginary: A) : ComplexNumber<A>(ring, ring.zero, imaginary) {

    override fun plus(y: ComplexNumber<A>): ComplexNumber<A> = when (y) {
        is RealComplexNumber      -> ComplexNumber(ring, y.real, this.imaginary)
        is ImaginaryComplexNumber -> ImaginaryComplexNumber(ring, ring.add(this.imaginary, y.imaginary))
        else                      -> ComplexNumber(ring, y.real, ring.add(this.imaginary, y.imaginary))
    }

    override fun unaryMinus(): ComplexNumber<A> = ImaginaryComplexNumber(ring, ring.negate(imaginary))

    override fun minus(y: ComplexNumber<A>): ComplexNumber<A> = when (y) {
        is RealComplexNumber      -> ComplexNumber(ring, ring.negate(y.real), this.imaginary)
        is ImaginaryComplexNumber -> ImaginaryComplexNumber(ring, ring.subtract(this.imaginary, y.imaginary))
        else                      -> ComplexNumber(ring, ring.negate(y.real), ring.subtract(this.imaginary, y.imaginary))
    }

    override fun times(y: ComplexNumber<A>): ComplexNumber<A> = when (y) {
        is RealComplexNumber      -> ImaginaryComplexNumber(ring, ring.multiply(y.real, this.imaginary))
        is ImaginaryComplexNumber -> RealComplexNumber(ring, ring.negate(ring.multiply(this.imaginary, y.imaginary)))
        else                      -> ComplexNumber(ring, ring.negate(ring.multiply(this.imaginary, y.imaginary)), ring.multiply(this.imaginary, y.real))
    }

    override fun conjugate(): ComplexNumber<A> = ImaginaryComplexNumber(ring, ring.negate(imaginary))

    override fun lengthSquared(): A = ring.multiply(imaginary, imaginary)

    override fun hasInverse(): Boolean = ring.hasInverse(imaginary)

    override fun inverse(): ComplexNumber<A> = ImaginaryComplexNumber(ring, ring.negate(ring.inverse(imaginary)))

}