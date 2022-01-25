package math.abstract_structure.instance

import math.abstract_structure.Ring
import math.integer.modular.UIntModular

/**
 * Created by CowardlyLion at 2022/1/25 17:38
 *
 * may not optimal for computation, but safe.
 * UInt modulo 0 is empty set thus cannot perform any operation.
 */
class RingModularUInt(val modulus: UInt) : Ring<UIntModular> {
    override val descriptions: MutableSet<String> = mutableSetOf("ring of integer modulo $modulus")
    override val zero: UIntModular = UIntModular(modulus, 0u)
    override val one: UIntModular = UIntModular(modulus, 1u)

    override fun add(x: UIntModular, y: UIntModular): UIntModular {
        require(x.modulus == modulus)
        return x + y    //already checking modulus of x and y equal
    }

    override fun negate(a: UIntModular): UIntModular {
        require(a.modulus == modulus)
        return -a
    }

    override fun subtract(x: UIntModular, y: UIntModular): UIntModular {
        require(x.modulus == modulus)
        return x - y
    }

    override fun multiply(x: UIntModular, y: UIntModular): UIntModular {
        require(x.modulus == modulus)
        return x * y
    }

    override fun hasInverse(a: UIntModular): Boolean {
        require(a.modulus == modulus)
        return a.hasInverse()
    }

    override fun inverse(a: UIntModular): UIntModular {
        require(a.modulus == modulus)
        return a.inverse()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RingModularUInt) return false

        if (modulus != other.modulus) return false

        return true
    }

    override fun hashCode(): Int {
        return modulus.hashCode()
    }


}