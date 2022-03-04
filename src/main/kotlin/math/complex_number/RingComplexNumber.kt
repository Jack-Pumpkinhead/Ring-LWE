package math.complex_number

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/25 17:42
 */
open class RingComplexNumber<A>(val ring: Ring<A>) : Ring<ComplexNumber<A>> {

    override val descriptions: MutableSet<String> = ring.descriptions.mapTo(mutableSetOf()) { "R[x]/(x^2+1) over {$it}" }
    override val zero: ComplexNumber<A> = ring.complexNumber(ring.zero, ring.zero)
    override val one: ComplexNumber<A> = ring.complexNumber(ring.one, ring.zero)

    override fun add(x: ComplexNumber<A>, y: ComplexNumber<A>): ComplexNumber<A> {
        require(this.ring == x.ring)
        return x + y    //already checking ring of x and y equal here.
    }

    override fun negate(a: ComplexNumber<A>): ComplexNumber<A> {
        require(this.ring == a.ring)
        return -a
    }

    override fun subtract(x: ComplexNumber<A>, y: ComplexNumber<A>): ComplexNumber<A> {
        require(this.ring == x.ring)
        return x - y
    }

    override fun multiply(x: ComplexNumber<A>, y: ComplexNumber<A>): ComplexNumber<A> {
        require(this.ring == x.ring)
        return x * y
    }

    override fun hasInverse(a: ComplexNumber<A>): Boolean {
        require(this.ring == a.ring)
        return a.hasInverse()
    }

    override fun inverse(a: ComplexNumber<A>): ComplexNumber<A> {
        require(this.ring == a.ring)
        return a.inverse()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RingComplexNumber<*>) return false

        if (ring != other.ring) return false

        return true
    }

    override fun hashCode(): Int {
        return ring.hashCode()
    }

    override fun ofInteger(a: UInt): ComplexNumber<A> = ring.realComplexNumber(ring.ofInteger(a))
    override fun ofInteger(a: Int): ComplexNumber<A> = ring.realComplexNumber(ring.ofInteger(a))
    override fun ofInteger(a: ULong): ComplexNumber<A> = ring.realComplexNumber(ring.ofInteger(a))
    override fun ofInteger(a: Long): ComplexNumber<A> = ring.realComplexNumber(ring.ofInteger(a))

    override val isExactComputation: Boolean get() = ring.isExactComputation
}