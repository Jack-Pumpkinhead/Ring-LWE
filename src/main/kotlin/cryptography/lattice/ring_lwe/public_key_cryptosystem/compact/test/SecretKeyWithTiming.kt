package cryptography.lattice.ring_lwe.public_key_cryptosystem.compact.test

import cryptography.lattice.ring_lwe.integerCenteredRounding
import cryptography.lattice.ring_lwe.public_key_cryptosystem.compact.EncryptedMessage
import math.integer.uint.modular.ModularUInt
import math.map
import math.martix.AbstractColumnVector

/**
 * Created by CowardlyLion at 2022/4/27 18:22
 */
class SecretKeyWithTiming(val system: CryptosystemWithTiming, val x: AbstractColumnVector<ModularUInt>) {

    fun decrypt(encrypted: EncryptedMessage): AbstractColumnVector<ModularUInt> {
        system.apply {
            val (u, v) = encrypted
            val ux = qMultiplyInCe(u, x)
            val v_ux = v.subtract(ux)
            val toDecode = v_ux.ceToPower().qGPowerToDecode()
            val decode_mod_p = toDecode.map(p) { p.ofInteger(integerCenteredRounding(it.residue.toInt(), q.modulus)) }
            return decode_mod_p.pDecodeToGPower()
        }
    }

}