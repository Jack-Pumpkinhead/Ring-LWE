package math.integer.uint.modular

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/25 17:38
 *
 * may not optimal for computation, but safe.
 * UInt modulo 0 is empty set thus cannot perform any operation.
 */
open class RingModularUInt(val modulus: UInt) : Ring<ModularUInt> {

    override val descriptions: MutableSet<String> = mutableSetOf("ring of integer modulo $modulus")
    override val zero: ModularUInt = ModularUInt(modulus, 0u)
    override val one: ModularUInt = ModularUInt(modulus, 1u)

    override fun add(x: ModularUInt, y: ModularUInt): ModularUInt {
        require(x.modulus == modulus)
        return x + y    //already checking modulus of x and y equal
    }

    override fun negate(a: ModularUInt): ModularUInt {
        require(a.modulus == modulus)
        return -a
    }

    override fun subtract(x: ModularUInt, y: ModularUInt): ModularUInt {
        require(x.modulus == modulus)
        return x - y
    }

    override fun multiply(x: ModularUInt, y: ModularUInt): ModularUInt {
        require(x.modulus == modulus)
        return x * y
    }

    override fun hasInverse(a: ModularUInt): Boolean {
        require(a.modulus == modulus)
        return a.hasInverse()
    }

    override fun inverse(a: ModularUInt): ModularUInt {
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

    override fun ofInteger(a: UInt): ModularUInt = ModularUInt(modulus, a.mod(modulus))

    override fun ofInteger(a: Int): ModularUInt =
        if (a >= 0) {
            ModularUInt(modulus, a.toUInt().mod(modulus))
        } else if (modulus <= Int.MAX_VALUE.toULong()) {
            ModularUInt(modulus, a.mod(modulus.toInt()).toUInt())
        } else if (a == Int.MIN_VALUE) {
            ModularUInt(modulus, modulus - Int.MAX_VALUE.toUInt() - 1u)
        } else {
            ModularUInt(modulus, modulus - (-a).toUInt())
        }

    override fun ofInteger(a: ULong): ModularUInt = ModularUInt(modulus, a.mod(modulus))

    override fun ofInteger(a: Long): ModularUInt =
        if (a >= 0) {
            ModularUInt(modulus, a.toULong().mod(modulus))
        } else {
            ModularUInt(modulus, a.mod(modulus.toLong()).toUInt())
        }

    override val isExactComputation: Boolean get() = true
}