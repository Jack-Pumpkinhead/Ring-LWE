package cryptography.lattice.ring_lwe.public_key_cryptosystem.compact.test

import cryptography.lattice.ring_lwe.public_key_cryptosystem.compact.EncryptedMessage
import cryptography.lattice.ring_lwe.public_key_cryptosystem.randomlyNearestElement1
import cryptography.lattice.ring_lwe.public_key_cryptosystem.randomlyNearestInteger
import cryptography.lattice.ring_lwe.public_key_cryptosystem.samplingContinuousGaussianToDecodingBasisFast
import math.abstract_structure.instance.FieldDouble
import math.complex_number.roundToReal
import math.integer.long.RingLong
import math.integer.uint.modular.ModularUInt
import math.map
import math.martix.AbstractColumnVector
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Created by CowardlyLion at 2022/4/4 20:45
 *
 * [a], [b] in qCE basis
 */
@OptIn(ExperimentalTime::class)
class PublicKeyWithTiming(val system: CryptosystemWithTiming, val a: AbstractColumnVector<ModularUInt>, val b: AbstractColumnVector<ModularUInt>) {

    val times = mutableMapOf<String, Duration>()
    var callTimes = 0u

    var time: TimeMark = TimeSource.Monotonic.markNow()

    fun reset() {
        callTimes++
        time = TimeSource.Monotonic.markNow()
    }

    fun measure(name: String) {
        val timeDuration = time.elapsedNow()
        val duration = times[name]
        if (duration != null) {
            times[name] = duration + timeDuration
        } else {
            times[name] = timeDuration
        }
        time = TimeSource.Monotonic.markNow()
    }

    fun printTimes() {
        times.forEach { (name, t) -> println("$name: ${t / callTimes.toDouble()}") }
    }

    /**
     * [message] is mod p in power basis
     */
    fun encrypt(message: AbstractColumnVector<ModularUInt>): EncryptedMessage {
        system.apply {
            reset()
//            val e1_pPhi = random.samplingContinuousGaussianToDecodingBasis(order, 0.0, sigmaEncryptError).map(FieldDouble) { it.roundToReal() * p.modulus.toDouble() }
            val e1_pPhi = samplingContinuousGaussianToDecodingBasisFast(order, 0.0, sigmaEncryptError).map(FieldDouble) { it.roundToReal() * p.modulus.toDouble() }
            val e1_discrete = random.randomlyNearestInteger(e1_pPhi, p.modulus)
            val e1_q = e1_discrete.map(q) { q.ofInteger(it) } //decoding basis
            val e1 = e1_q.qDecodeToGPower().powerToCe()    //Gce basis
            measure("a")

//            val z0 = random.samplingContinuousGaussianToDecodingBasis(order, 0.0, sigmaEncryptZ).map(FieldDouble) { it.roundToReal() }
            val z0 = samplingContinuousGaussianToDecodingBasisFast(order, 0.0, sigmaEncryptZ).map(FieldDouble) { it.roundToReal() }
            measure("b1")
            val z_discrete = random.randomlyNearestInteger(z0)
            measure("b2")
            val z_q = z_discrete.map(q) { q.ofInteger(it) } //decoding basis
            measure("b3")
            val z = z_q.qDecodeToGPower().powerToCe()    //Gce basis
            measure("b4")
            val az = qMultiplyInCe(a, z)
            measure("b5")
            val az_e1 = az.plus(e1) //implicitly Gce -> ce  (multiply by t)
            measure("b6")
            val u = qMultiplyInCe(g, az_e1)
//            val u = az_e1   //not affect correctness if g set to 1, but change one g only lower success rate.
            measure("b")

            val gm_decoding = message.pGPowerToDecode()
//            val e2_pPhi = random.samplingContinuousGaussianToDecodingBasis(order, 0.0, sigmaEncryptErrorM).map(FieldDouble) { it.roundToReal() * p.modulus.toDouble() }
            val e2_pPhi = samplingContinuousGaussianToDecodingBasisFast(order, 0.0, sigmaEncryptErrorM).map(FieldDouble) { it.roundToReal() * p.modulus.toDouble() }
            val e2_discrete = random.randomlyNearestElement1(e2_pPhi, gm_decoding.map(RingLong) { it.residue.toLong() }, p.modulus)
            val e2_q = e2_discrete.map(q) { q.ofInteger(it) } //decoding basis
            val e2 = e2_q.qDecodeToGPower().powerToCe()    //Gce basis
            val bz = qMultiplyInCe(b, z)
            val v = bz.plus(e2)
            measure("c")
            return EncryptedMessage(u, v)
        }
    }

}