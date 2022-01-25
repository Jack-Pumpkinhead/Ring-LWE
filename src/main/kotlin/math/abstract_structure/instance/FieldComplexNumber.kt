package math.abstract_structure.instance

import math.abstract_structure.Field
import math.complex_number.ComplexNumber
import math.complex_number.complexNumber

/**
 * Created by CowardlyLion at 2022/1/25 17:50
 */
class FieldComplexNumber<A>(val field: Field<A>) : Field<ComplexNumber<A>> {
    override val descriptions: MutableSet<String> = field.descriptions.mapTo(mutableSetOf()) { "R[x]/(x^2+1) over ($it)" }
    override val zero: ComplexNumber<A> = field.complexNumber(field.zero, field.zero)
    override val one: ComplexNumber<A> = field.complexNumber(field.one, field.zero)

    override fun add(x: ComplexNumber<A>, y: ComplexNumber<A>): ComplexNumber<A> {
        require(this.field == x.ring)
        return x + y    //already checking ring of x and y equal here.
    }

    override fun negate(a: ComplexNumber<A>): ComplexNumber<A> {
        require(this.field == a.ring)
        return -a
    }

    override fun subtract(x: ComplexNumber<A>, y: ComplexNumber<A>): ComplexNumber<A> {
        require(this.field == x.ring)
        return x - y
    }

    override fun multiply(x: ComplexNumber<A>, y: ComplexNumber<A>): ComplexNumber<A> {
        require(this.field == x.ring)
        return x * y
    }

    override fun inverse(a: ComplexNumber<A>): ComplexNumber<A> {
        require(this.field == a.ring)
        return a.inverse()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FieldComplexNumber<*>) return false

        if (field != other.field) return false

        return true
    }

    override fun hashCode(): Int {
        return field.hashCode()
    }

}