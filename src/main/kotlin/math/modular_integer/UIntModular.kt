package math.modular_integer

import math.operations.modMinus
import math.operations.modPlus
import math.operations.modTimes

/**
 * Created by CowardlyLion at 2022/1/4 15:20
 */
class UIntModular(val modulus: UInt, val residue: UInt) {

    operator fun plus(y: UIntModular): UIntModular {
        assert(this.modulus == y.modulus)
        return UIntModular(modulus, modPlus(residue, y.residue, modulus))
    }

    operator fun minus(y: UIntModular): UIntModular {
        assert(this.modulus == y.modulus)
        return UIntModular(modulus, modMinus(residue, y.residue, modulus))
    }

    operator fun times(y: UIntModular): UIntModular {
        assert(this.modulus == y.modulus)
        return UIntModular(modulus, modTimes(residue, y.residue, modulus))
    }


}