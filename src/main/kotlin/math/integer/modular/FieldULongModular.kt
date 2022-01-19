package math.integer.modular

import math.abstract_structure.Field

/**
 * Created by CowardlyLion at 2022/1/19 23:34
 */
class FieldULongModular(val prime: ULong) : Field<ULongModular> {

    override val descriptions: MutableSet<String> = mutableSetOf("field of integer modulo $prime")
    override val zero: ULongModular = ULongModular(prime, 0uL)
    override val one: ULongModular = ULongModular(prime, 1uL)

    override fun add(x: ULongModular, y: ULongModular): ULongModular {
        require(x.modulus == prime)
        return x + y    //already checking modulus of x and y equal
    }

    override fun negate(a: ULongModular): ULongModular {
        require(a.modulus == prime)
        return -a
    }

    override fun subtract(x: ULongModular, y: ULongModular): ULongModular {
        require(x.modulus == prime)
        return x - y
    }

    override fun multiply(x: ULongModular, y: ULongModular): ULongModular {
        require(x.modulus == prime)
        return x * y
    }

    override fun inverse(a: ULongModular): ULongModular {
        require(a.modulus == prime)
        return a.inverse()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FieldULongModular) return false

        if (prime != other.prime) return false

        return true
    }

    override fun hashCode(): Int {
        return prime.hashCode()
    }


}