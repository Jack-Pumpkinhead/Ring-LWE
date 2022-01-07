package math.modular_integer

import math.operations.modMinus
import math.operations.modPlus
import math.operations.modTimes

/**
 * Created by CowardlyLion at 2022/1/4 15:20
 */
class UIntModular(val modulus: UInt, val residue: UInt) {

    init {
        require(residue in 0u until modulus)
    }

    operator fun plus(y: UIntModular): UIntModular {
        require(this.modulus == y.modulus)
        return UIntModular(modulus, modPlus(residue, y.residue, modulus))
    }

    operator fun minus(y: UIntModular): UIntModular {
        require(this.modulus == y.modulus)
        return UIntModular(modulus, modMinus(residue, y.residue, modulus))
    }

    operator fun times(y: UIntModular): UIntModular {
        require(this.modulus == y.modulus)
        return UIntModular(modulus, modTimes(residue, y.residue, modulus))
    }

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


}