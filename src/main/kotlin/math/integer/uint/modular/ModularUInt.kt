package math.integer.uint.modular

import math.integer.uint.*

/**
 * Created by CowardlyLion at 2022/1/4 15:20
 *
 * no overflow of multiplication would occur
 */
class ModularUInt(val modulus: UInt, val residue: UInt) {

    init {
        require(residue in 0u until modulus)
    }

    operator fun plus(y: ModularUInt): ModularUInt {
        require(this.modulus == y.modulus)
        return ModularUInt(modulus, modPlusUnsafe(residue, y.residue, modulus))
    }

    operator fun unaryMinus() = ModularUInt(modulus, modUnaryMinusUnsafe(residue, modulus))

    operator fun minus(y: ModularUInt): ModularUInt {
        require(this.modulus == y.modulus)
        return ModularUInt(modulus, modMinusUnsafe(residue, y.residue, modulus))
    }

    operator fun times(y: ModularUInt): ModularUInt {
        require(this.modulus == y.modulus)
        return ModularUInt(modulus, modTimesUnsafe(residue, y.residue, modulus))
    }

    fun hasInverse(): Boolean = gcd(modulus, residue) == 1u

    fun inverse(): ModularUInt = ModularUInt(modulus, residue.modInverse(modulus))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ModularUInt) return false

        if (modulus != other.modulus) return false
        if (residue != other.residue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = modulus.hashCode()
        result = 31 * result + residue.hashCode()
        return result
    }

    override fun toString(): String {
        return residue.toString()
    }

}