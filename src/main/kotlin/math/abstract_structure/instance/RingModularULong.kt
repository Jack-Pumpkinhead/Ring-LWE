package math.abstract_structure.instance

import math.abstract_structure.Ring
import math.integer.modular.ModularULong

/**
 * Created by CowardlyLion at 2022/1/19 23:30
 */
open class RingModularULong(val modulus: ULong) : Ring<ModularULong> {

    override val descriptions: MutableSet<String> = mutableSetOf("ring of integer modulo $modulus")
    override val zero: ModularULong = ModularULong(modulus, 0uL)
    override val one: ModularULong = ModularULong(modulus, 1uL)

    override fun add(x: ModularULong, y: ModularULong): ModularULong {
        require(x.modulus == modulus)
        return x + y    //already checking modulus of x and y equal
    }

    override fun negate(a: ModularULong): ModularULong {
        require(a.modulus == modulus)
        return -a
    }

    override fun subtract(x: ModularULong, y: ModularULong): ModularULong {
        require(x.modulus == modulus)
        return x - y
    }

    override fun multiply(x: ModularULong, y: ModularULong): ModularULong {
        require(x.modulus == modulus)
        return x * y
    }

    override fun hasInverse(a: ModularULong): Boolean {
        require(a.modulus == modulus)
        return a.hasInverse()
    }

    override fun inverse(a: ModularULong): ModularULong {
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