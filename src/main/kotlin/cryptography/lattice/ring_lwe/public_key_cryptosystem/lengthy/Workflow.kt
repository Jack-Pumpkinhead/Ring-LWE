package cryptography.lattice.ring_lwe.public_key_cryptosystem.lengthy

import cryptography.lattice.ring_lwe.integerCenteredRounding
import cryptography.lattice.ring_lwe.matrix.LowerTriangularOneMatrix
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.modular_uint.CeMatrixBuilderModularUInt
import cryptography.lattice.ring_lwe.public_key_cryptosystem.approximatelySamplingDiscreteGaussianOnOriginToPowerBasis
import cryptography.lattice.ring_lwe.public_key_cryptosystem.randomlyNearestElement1
import cryptography.lattice.ring_lwe.public_key_cryptosystem.samplingContinuousGaussianToDecodingBasis
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafeModularUInt
import math.abstract_structure.instance.FieldDouble
import math.complex_number.roundToReal
import math.integer.long.RingLong
import math.integer.uint.factored.UIntPPI
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.uint.modular.RingModularUInt
import math.map
import math.martix.AbstractColumnVector
import math.martix.columnVector
import math.martix.concrete.ColumnVector
import math.martix.concrete.view.ColumnVectorView
import math.martix.whiskered
import math.random.randomColumnVector
import util.stdlib.list
import util.stdlib.mutableList
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/2/27 21:58
 *
 * assume:
 *     order | q-1
 *     q is prime
 */
class Workflow(
    val random: Random,
    val order: UIntPPI,
    val length: UInt,
    val sigmaKey: Double,
    val q: FieldModularUInt,
    val p: RingModularUInt,
    val sigmaEncrypt: Double
) {

    val dimension = order.eulerTotient

    fun FieldModularUInt.multiplyInCe(a: AbstractColumnVector<ModularUInt>, b: AbstractColumnVector<ModularUInt>): AbstractColumnVector<ModularUInt> {
        return columnVector(dimension) { i -> q.multiply(a.vectorElementAtUnsafe(i), b.vectorElementAtUnsafe(i)) }
    }

    val root = SubrootCalculatorUnsafeModularUInt.compute(q.firstGenerator, order)

    val ce = CeMatrixBuilderModularUInt.compute(root)

    fun AbstractColumnVector<ModularUInt>.toCe() = (ce * this).columnVectorViewAt(0u)   //TODO remove columnVectorViewAt(0u)
    fun AbstractColumnVector<ModularUInt>.ceInv() = (ce.inverse * this).columnVectorViewAt(0u)

    fun generateKeys(): Pair<PublicKey, SecretKey> {

        val x_ = random.approximatelySamplingDiscreteGaussianOnOriginToPowerBasis(order, sigmaKey)
        val secretKey = list(length - 1u) { random.approximatelySamplingDiscreteGaussianOnOriginToPowerBasis(order, sigmaKey) }    //power basis
        val secretKeyModQ = secretKey.map { x -> ColumnVector(q, x.map { q.ofInteger(it) }) }
        val publicKey_ce: MutableList<AbstractColumnVector<ModularUInt>> = mutableList(length - 1u) { q.randomColumnVector(dimension) }   //ce basis


        val secretKeyModQ_ce: List<ColumnVectorView<ModularUInt>> = secretKeyModQ.map { it.toCe() }    //ce basis

        val x_ModQ_ce = ColumnVector(q, x_.map { q.ofInteger(it) }).toCe()

        var sum: AbstractColumnVector<ModularUInt> = q.columnVector(dimension) { q.zero }
        for (i in 0u until secretKeyModQ_ce.size.toUInt()) {
            val x = secretKeyModQ_ce[i.toInt()]
            val a = publicKey_ce[i.toInt()]
            sum = sum.plus(q.multiplyInCe(x, a))
        }

        publicKey_ce += x_ModQ_ce.subtract(sum)

        return PublicKey(publicKey_ce) to SecretKey(secretKeyModQ_ce)
    }

    val publicKey: PublicKey
    val secretKey: SecretKey

    init {
        val (publicKey1, secretKey1) = generateKeys()
        publicKey = publicKey1
        secretKey = secretKey1
    }

    val pGPowerToDecoding = p.whiskered(dimension, 1u, LowerTriangularOneMatrix(p, order.prime - 1u).inverse, order.value / order.prime)
    val pDecodingToGPower = p.whiskered(dimension, 1u, LowerTriangularOneMatrix(p, order.prime - 1u), order.value / order.prime)
    fun AbstractColumnVector<ModularUInt>.pGPowerToDecoding() = (pGPowerToDecoding * this).columnVectorViewAt(0u)
    fun AbstractColumnVector<ModularUInt>.pDecodingToGPower() = (pDecodingToGPower * this).columnVectorViewAt(0u)

    val qDecodeToGPower = q.whiskered(dimension, 1u, LowerTriangularOneMatrix(q, order.prime - 1u), order.value / order.prime)
    fun AbstractColumnVector<ModularUInt>.qDecodeToGPower() = (qDecodeToGPower * this).columnVectorViewAt(0u)

    /**
     * [message] is mod p in power basis
     */
    fun encrypt(message: List<ModularUInt>): MutableList<AbstractColumnVector<ModularUInt>> {
        val phi_ = random.samplingContinuousGaussianToDecodingBasis(order, 0.0, sigmaEncrypt).map(FieldDouble) { it.roundToReal() * p.modulus.toDouble() }
        val phis = list(length - 1u) { random.samplingContinuousGaussianToDecodingBasis(order, 0.0, sigmaEncrypt) }.map { phi ->
            phi.map(FieldDouble) { it.roundToReal() * p.modulus.toDouble() }
        }
        val phiLast = random.samplingContinuousGaussianToDecodingBasis(order, 0.0, sigmaEncrypt).map(FieldDouble) { it.roundToReal() * p.modulus.toDouble() }

        val discrete_ce = random.randomlyNearestElement1(phi_, RingLong.columnVector(dimension) { 0L }, p.modulus).map(q) { q.ofInteger(it) }.qDecodeToGPower().toCe()
        val discretes_ce = phis.map { phi -> random.randomlyNearestElement1(phi, RingLong.columnVector(dimension) { 0L }, p.modulus).map(q) { q.ofInteger(it) }.qDecodeToGPower().toCe() }

        val gm_decoding = ColumnVector(p, message).pGPowerToDecoding()
        val discreteLast_ce = random.randomlyNearestElement1(phiLast, gm_decoding.map(RingLong) { it.residue.toLong() }, p.modulus).map(q) { q.ofInteger(it) }.qDecodeToGPower().toCe()

        val cyphertext = mutableList(length - 1u) { i ->
            q.multiplyInCe(discrete_ce, publicKey.key[i.toInt()]).plus(discretes_ce[i.toInt()])
        }
        cyphertext += q.multiplyInCe(discrete_ce, publicKey.key.last()).plus(discreteLast_ce)

        return cyphertext
    }

    val qGPowerToDecoding = q.whiskered(dimension, 1u, LowerTriangularOneMatrix(q, order.prime - 1u).inverse, order.value / order.prime)
    fun AbstractColumnVector<ModularUInt>.qGPowerToDecoding() = (qGPowerToDecoding * this).columnVectorViewAt(0u)


    /**
     * [cyphertext] is mod q in ce basis
     */
    fun decrypt(cyphertext: List<AbstractColumnVector<ModularUInt>>): List<ModularUInt> {
        var sum: AbstractColumnVector<ModularUInt> = q.columnVector(dimension) { q.zero }
        for (i in 0u until length - 1u) {
            sum = sum.plus(q.multiplyInCe(secretKey.key[i.toInt()], cyphertext[i.toInt()]))
        }
        sum = sum.plus(cyphertext.last())
        val sum_deco = sum.ceInv().qGPowerToDecoding()
        val sum_deco_round = sum_deco.columnListAt(0u).map { integerCenteredRounding(it.residue.toInt(), q.modulus) }    //TODO write integerCenteredRounding directly in ModulusUInt
        val sum_deco_modP = ColumnVector(p, sum_deco_round.map { p.ofInteger(it) })
        val message = sum_deco_modP.pDecodingToGPower()
        return message.columnListAt(0u)
    }

}