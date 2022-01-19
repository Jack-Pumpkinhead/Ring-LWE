package math.integer.modular

import math.integer.gcd
import math.integer.modInverse
import math.integer.operation.*

/**
 * Created by CowardlyLion at 2022/1/4 15:20
 */
class UIntModular(val modulus: UInt, val residue: UInt) {

    init {
        require(residue in 0u until modulus)
    }

    operator fun plus(y: UIntModular): UIntModular {
        require(this.modulus == y.modulus)
        return UIntModular(modulus, modPlusUnsafe(residue, y.residue, modulus))
    }

    operator fun unaryMinus() = UIntModular(modulus, modUnaryMinusUnsafe(residue, modulus))

    operator fun minus(y: UIntModular): UIntModular {
        require(this.modulus == y.modulus)
        return UIntModular(modulus, modMinusUnsafe(residue, y.residue, modulus))
    }

    operator fun times(y: UIntModular): UIntModular {
        require(this.modulus == y.modulus)
        return UIntModular(modulus, modTimesUnsafe(residue, y.residue, modulus))
    }

    fun hasInverse(): Boolean = gcd(modulus, residue) == 1u

    fun inverse(): UIntModular = UIntModular(modulus, residue.modInverse(modulus))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UIntModular) return false

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