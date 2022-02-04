package math.complex_number

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/17 16:36
 *
 * modeled as polynomial ring R{x}/(x^2+1)
 */
open class ComplexNumber<A>(val ring: Ring<A>, val real: A, val imaginary: A) {

    open operator fun plus(y: ComplexNumber<A>): ComplexNumber<A> {
        require(this.ring == y.ring)
        return ComplexNumber(ring, ring.add(this.real, y.real), ring.add(this.imaginary, y.imaginary))
    }

    open operator fun unaryMinus() = ComplexNumber(ring, ring.negate(real), ring.negate(imaginary))

    open operator fun minus(y: ComplexNumber<A>): ComplexNumber<A> {
        require(this.ring == y.ring)
        return ComplexNumber(ring, ring.subtract(this.real, y.real), ring.subtract(this.imaginary, y.imaginary))
    }

    //    (a+bi)(c+di) = (ac-bd) + (ad+bc)i
    open operator fun times(y: ComplexNumber<A>): ComplexNumber<A> {
        require(this.ring == y.ring)
        return ComplexNumber(
            ring,
            ring.subtract(ring.multiply(this.real, y.real), ring.multiply(this.imaginary, y.imaginary)),
            ring.add(ring.multiply(this.real, y.imaginary), ring.multiply(this.imaginary, y.real))
        )
    }

    open fun conjugate() = ComplexNumber(ring, real, ring.negate(imaginary))

    open fun lengthSquared() = ring.add(ring.multiply(real, real), ring.multiply(imaginary, imaginary))

    open fun hasInverse(): Boolean = ring.hasInverse(lengthSquared())

    //        (a+bi)^-1 = (a-bi)/(a^2+b^2)
    open fun inverse(): ComplexNumber<A> {
        val lengthSquareInversed = ring.inverse(lengthSquared())
        return ComplexNumber(ring, ring.multiply(real, lengthSquareInversed), ring.negate(ring.multiply(imaginary, lengthSquareInversed)))
    }

    override fun toString(): String {
        return "$real + i $imaginary"
    }
}