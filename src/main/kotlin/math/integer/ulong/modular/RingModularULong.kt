package math.integer.ulong.modular

import math.abstract_structure.Ring

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

    override fun ofInteger(a: UInt): ModularULong = ModularULong(modulus, a.mod(modulus))

    override fun ofInteger(a: Int): ModularULong =
        if (a >= 0) {
            ModularULong(modulus, a.toUInt().mod(modulus))
        } else if (modulus <= Long.MAX_VALUE.toULong()) {
            ModularULong(modulus, a.mod(modulus.toLong()).toULong())
        } else if (a == Int.MIN_VALUE) {
            ModularULong(modulus, modulus - Int.MAX_VALUE.toULong() - 1uL)
        } else {
            ModularULong(modulus, modulus - (-a).toULong())
        }

    override fun ofInteger(a: ULong): ModularULong = ModularULong(modulus, a.mod(modulus))

    override fun ofInteger(a: Long): ModularULong =
        if (a >= 0) {
            ModularULong(modulus, a.toULong().mod(modulus))
        } else if (modulus <= Long.MAX_VALUE.toULong()) {
            ModularULong(modulus, a.mod(modulus.toLong()).toULong())
        } else if (a == Long.MIN_VALUE) {
            ModularULong(modulus, modulus - Long.MAX_VALUE.toULong() - 1uL)
        } else {
            ModularULong(modulus, modulus - (-a).toULong())
        }

}