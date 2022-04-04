package cryptography.lattice.ring_lwe.public_key_cryptosystem.compact

import cryptography.lattice.ring_lwe.public_key_cryptosystem.randomlyNearestElement1
import cryptography.lattice.ring_lwe.public_key_cryptosystem.randomlyNearestInteger
import math.abstract_structure.instance.FieldDouble
import math.complex_number.roundToReal
import math.integer.long.RingLong
import math.integer.uint.modular.ModularUInt
import math.map
import math.martix.AbstractColumnVector

/**
 * Created by CowardlyLion at 2022/4/4 20:45
 *
 * [a], [b] in qCE basis
 */
class PublicKey(val system: Cryptosystem, val a: AbstractColumnVector<ModularUInt>, val b: AbstractColumnVector<ModularUInt>) {

    /**
     * [message] is mod p in power basis
     */
    fun encrypt(message: AbstractColumnVector<ModularUInt>): EncryptedMessage {
        system.apply {
            val e1_pPhi = random.samplingContinuousGaussianToDecodingBasis(order, 0.0, sigmaEncryptError).map(FieldDouble) { it.roundToReal() * p.modulus.toDouble() }
            val e1_discrete = random.randomlyNearestInteger(e1_pPhi, p.modulus)
            val e1_q = e1_discrete.map(q) { q.ofInteger(it) } //decoding basis
            val e1 = e1_q.qDecodeToGPower().powerToCe()    //Gce basis

            val z0 = random.samplingContinuousGaussianToDecodingBasis(order, 0.0, sigmaEncryptZ).map(FieldDouble) { it.roundToReal() }
            val z_discrete = random.randomlyNearestInteger(z0)
            val z_q = z_discrete.map(q) { q.ofInteger(it) } //decoding basis
            val z = z_q.qDecodeToGPower().powerToCe()    //Gce basis
            val az = qMultiplyInCe(a, z)
            val az_e1 = az.plus(e1) //implicitly Gce -> ce  (multiply by t)
            val u = qMultiplyInCe(g, az_e1)

            val gm_decoding = message.pGPowerToDecode()
            val e2_pPhi = random.samplingContinuousGaussianToDecodingBasis(order, 0.0, sigmaEncryptErrorM).map(FieldDouble) { it.roundToReal() * p.modulus.toDouble() }
            val e2_discrete = random.randomlyNearestElement1(e2_pPhi, gm_decoding.map(RingLong) { it.residue.toLong() }, p.modulus)
            val e2_q = e2_discrete.map(q) { q.ofInteger(it) } //decoding basis
            val e2 = e2_q.qDecodeToGPower().powerToCe()    //Gce basis
            val bz = qMultiplyInCe(b, z)
            val v = bz.plus(e2)
            return EncryptedMessage(u, v)
        }
    }

}