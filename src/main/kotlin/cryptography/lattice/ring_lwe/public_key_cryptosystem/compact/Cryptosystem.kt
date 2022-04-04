package cryptography.lattice.ring_lwe.public_key_cryptosystem.compact

import cryptography.lattice.ring_lwe.public_key_cryptosystem.randomlyNearestInteger
import math.abstract_structure.instance.FieldDouble
import math.coding.LadderIndex
import math.complex_number.roundToReal
import math.integer.uint.factored.UIntP
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.UIntPPP
import math.integer.uint.factored.UIntPPPI
import math.integer.uint.isCoprime
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.uint.modular.RingModularUInt
import math.map
import math.martix.AbstractColumnVector
import math.martix.columnVector
import math.martix.mutableColumnVector
import math.random.randomColumnVector
import util.errorUnknownObject
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/4/4 20:49
 *
 * assume prime of factors of [order] in increasing order.
 */
class Cryptosystem(
    val random: Random,
    val order: UIntPPPI,
    val q: FieldModularUInt,
    val p: RingModularUInt,
    val sigmaKeyError: Double,
    val sigmaSecretKey: Double,
    val sigmaEncryptError: Double,
    val sigmaEncryptErrorM: Double,
    val sigmaEncryptZ: Double,
) {

    val dimension = order.eulerTotient

    init {
        require(isCoprime(q.modulus, p.modulus))
        if (q.modulus != 2u) {
            require(isCoprime(q.modulus, order.value))
        }
    }

    fun qMultiplyInCe(a: AbstractColumnVector<ModularUInt>, b: AbstractColumnVector<ModularUInt>): AbstractColumnVector<ModularUInt> {
        return q.columnVector(dimension) { i -> q.multiply(a.vectorElementAtUnsafe(i), b.vectorElementAtUnsafe(i)) }
    }

    val ce = q.ceMatrix(order)
    fun AbstractColumnVector<ModularUInt>.powerToCe() = (ce * this).columnVectorViewAt(0u)
    fun AbstractColumnVector<ModularUInt>.ceToPower() = (ce.inverse * this).columnVectorViewAt(0u)

    val g = g() //in q-ce basis

    /**
     * ∏_p (1-ζp) where p is odd prime dividing [order]
     *
     * in q-ce basis
     */
    fun g(): AbstractColumnVector<ModularUInt> {
        return when (order) {
            is UIntPPP -> {
                val ladderIndex = LadderIndex(order.factors.map { it.eulerTotient }, dimension)
                val zeta_p = q.mutableColumnVector(dimension) { q.zero }    //in power basis
                var lastIndex: UInt? = null
                fun setOne(index: UInt) {
                    if (lastIndex != null) {
                        zeta_p.setVectorElementAtUnsafe(lastIndex!!, q.zero)
                    }
                    zeta_p.setVectorElementAtUnsafe(index, q.one)
                    lastIndex = index
                }

                var i = 0
                var factor = order.factors[i]
                if (order.factors[i].prime == 2u) {
                    i++
                    factor = order.factors[i]
                }
                val multiIndex = MutableList(order.factors.size) { 0u }
                multiIndex[i] = factor.value / factor.prime
                setOne(ladderIndex.encode(multiIndex))
                var zeta_p_ce = zeta_p.powerToCe()
                var g = zeta_p_ce.map(q) { q.subtract(q.one, it) }
                i++
                while (i < order.factors.size) {
                    factor = order.factors[i]
                    multiIndex[i - 1] = 0u
                    multiIndex[i] = factor.value / factor.prime
                    setOne(ladderIndex.encode(multiIndex))
                    zeta_p_ce = zeta_p.powerToCe()
                    val g0 = zeta_p_ce.map(q) { q.subtract(q.one, it) }
                    g = qMultiplyInCe(g, g0)
                    i++
                }
                g
            }
            is UIntPP  -> {
                if (order.prime == 2u) {
                    q.columnVector(dimension) { q.one }
                } else {
                    val one_minus_zeta_p = q.mutableColumnVector(dimension) { q.zero }
                    one_minus_zeta_p.setVectorElementAtUnsafe(0u, q.one)
                    val exponent = order.value / order.prime
                    one_minus_zeta_p.setVectorElementAtUnsafe(exponent, q.ofInteger(-1))
                    one_minus_zeta_p
                }
            }
            is UIntP   -> {
                if (order.prime == 2u) {
                    q.columnVector(dimension) { q.one }
                } else {
                    val one_minus_zeta_p = q.mutableColumnVector(dimension) { q.zero }
                    one_minus_zeta_p.setVectorElementAtUnsafe(0u, q.one)
                    one_minus_zeta_p.setVectorElementAtUnsafe(1u, q.ofInteger(-1))
                    one_minus_zeta_p
                }
            }
            else       -> errorUnknownObject(order)
        }
    }

    val qDecodeToGPower = q.decodeToGPower(order)
    fun AbstractColumnVector<ModularUInt>.qDecodeToGPower() = (qDecodeToGPower * this).columnVectorViewAt(0u)

    val qGPowerToDecode = q.fromGPowerToDecode(order)
    fun AbstractColumnVector<ModularUInt>.qGPowerToDecode() = (qGPowerToDecode * this).columnVectorViewAt(0u)

    val pDecodeToGPower = p.decodeToGPower(order)
    fun AbstractColumnVector<ModularUInt>.pDecodeToGPower() = (pDecodeToGPower * this).columnVectorViewAt(0u)

    val pGPowerToDecode = p.fromGPowerToDecode(order)
    fun AbstractColumnVector<ModularUInt>.pGPowerToDecode() = (pGPowerToDecode * this).columnVectorViewAt(0u)


    fun generateKeys(): Pair<PublicKey, SecretKey> {
        val a = q.randomColumnVector(dimension) //ce basis
        val x0 = random.samplingContinuousGaussianToDecodingBasis(order, 0.0, sigmaSecretKey).map(FieldDouble) { it.roundToReal() }
        val x_discrete = random.randomlyNearestInteger(x0)
        val x_q = x_discrete.map(q) { q.ofInteger(it) } //decoding basis
        val x = x_q.qDecodeToGPower().powerToCe()    //Gce basis
        val ax = qMultiplyInCe(a, x)

        val e_pPhi = random.samplingContinuousGaussianToDecodingBasis(order, 0.0, sigmaKeyError).map(FieldDouble) { it.roundToReal() * p.modulus.toDouble() }
        val e_discrete = random.randomlyNearestInteger(e_pPhi, p.modulus)
        val e_q = e_discrete.map(q) { q.ofInteger(it) } //decoding basis
        val e = e_q.qDecodeToGPower().powerToCe()    //Gce basis

        val ax_e = ax.plus(e)   //implicitly Gce -> ce  (multiply by t)
        val b = qMultiplyInCe(g, ax_e)

        return PublicKey(this, a, b) to SecretKey(this, x)
    }


}