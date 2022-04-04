package cryptography.lattice.ring_lwe.public_key_cryptosystem.compact

import math.integer.uint.modular.ModularUInt
import math.martix.AbstractColumnVector

/**
 * Created by CowardlyLion at 2022/4/4 23:48
 *
 * [u] in R/qR CE basis, [v] in dual(R)/q*dual(R) CE basis
 */
data class EncryptedMessage(val u: AbstractColumnVector<ModularUInt>, val v: AbstractColumnVector<ModularUInt>) {

    override fun toString(): String {
        return "u: ${u.columnListAt(0u)}, v: ${v.columnListAt(0u)}"
    }
}