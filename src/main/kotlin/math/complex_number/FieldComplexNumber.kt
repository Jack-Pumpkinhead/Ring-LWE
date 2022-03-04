package math.complex_number

import math.abstract_structure.Field

/**
 * Created by CowardlyLion at 2022/1/25 17:50
 */
open class FieldComplexNumber<A>(ring: Field<A>) : RingComplexNumber<A>(ring), Field<ComplexNumber<A>> {

    override val descriptions: MutableSet<String> = ring.descriptions.mapTo(mutableSetOf()) { "R[x]/(x^2+1) over ($it)" }
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

    override fun hasInverse(a: ComplexNumber<A>): Boolean = a != zero

    override fun inverse(a: ComplexNumber<A>): ComplexNumber<A> {
        require(this.ring == a.ring)
        return a.inverse()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FieldComplexNumber<*>) return false

        if (ring != other.ring) return false

        return true
    }

    override fun hashCode(): Int {
        return ring.hashCode()
    }

    override val isExactComputation: Boolean get() = ring.isExactComputation
}