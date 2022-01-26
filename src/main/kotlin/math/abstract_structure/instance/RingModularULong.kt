package math.abstract_structure.instance

import math.abstract_structure.Ring
import math.integer.modular.ULongModular

/**
 * Created by CowardlyLion at 2022/1/19 23:30
 */
class RingModularULong(val modulus: ULong) : Ring<ULongModular> {

    override val descriptions: MutableSet<String> = mutableSetOf("ring of integer modulo $modulus")
    override val zero: ULongModular = ULongModular(modulus, 0uL)
    override val one: ULongModular = ULongModular(modulus, 1uL)

    override fun add(x: ULongModular, y: ULongModular): ULongModular {
        require(x.modulus == modulus)
        return x + y    //already checking modulus of x and y equal
    }

    override fun negate(a: ULongModular): ULongModular {
        require(a.modulus == modulus)
        return -a
    }

    override fun subtract(x: ULongModular, y: ULongModular): ULongModular {
        require(x.modulus == modulus)
        return x - y
    }

    override fun multiply(x: ULongModular, y: ULongModular): ULongModular {
        require(x.modulus == modulus)
        return x * y
    }

    override fun hasInverse(a: ULongModular): Boolean {
        require(a.modulus == modulus)
        return a.hasInverse()
    }

    override fun inverse(a: ULongModular): ULongModular {
        require(a.modulus == modulus)
        return a.inverse()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RingModularULong) return false

        if (modulus != other.modulus) return false

        return true
    }

    override fun hashCode(): Int {
        return modulus.hashCode()
    }


}