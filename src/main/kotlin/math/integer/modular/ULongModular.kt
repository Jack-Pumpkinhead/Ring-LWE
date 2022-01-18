package math.integer.modular

import math.integer.gcd
import math.integer.modInverse
import math.integer.operation.*

/**
 * Created by CowardlyLion at 2022/1/18 18:06
 */
class ULongModular(val modulus: ULong, val residue: ULong) {


    init {
        require(residue in 0uL until modulus)
    }

    operator fun plus(y: ULongModular): ULongModular {
        require(this.modulus == y.modulus)
        return ULongModular(modulus, modPlusUnsafe(residue, y.residue, modulus))
    }

    operator fun unaryMinus() = ULongModular(modulus, modUnaryMinusUnsafe(residue, modulus))

    operator fun minus(y: ULongModular): ULongModular {
        require(this.modulus == y.modulus)
        return ULongModular(modulus, modMinusUnsafe(residue, y.residue, modulus))
    }

    operator fun times(y: ULongModular): ULongModular {
        require(this.modulus == y.modulus)
        return ULongModular(modulus, modTimesUnsafe(residue, y.residue, modulus))
    }

    fun hasInverse(): Boolean = gcd(modulus, residue) == 1uL

    fun inverse(): ULongModular = ULongModular(modulus, residue.modInverse(modulus))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ULongModular) return false

        if (modulus != other.modulus) return false
        if (residue != other.residue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = modulus.hashCode()
        result = 31 * result + residue.hashCode()
        return result
    }


}