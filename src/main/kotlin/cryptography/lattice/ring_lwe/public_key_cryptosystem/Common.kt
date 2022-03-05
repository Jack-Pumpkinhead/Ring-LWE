package cryptography.lattice.ring_lwe.public_key_cryptosystem

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.complex_double.CeCTMatrixCreatorComplexDouble
import cryptography.lattice.ring_lwe.randomlySelectNearZero
import math.abstract_structure.instance.FieldDouble
import math.complex_number.ComplexNumber
import math.complex_number.FieldComplexNumberDouble
import math.complex_number.realComplexNumber
import math.integer.long.RingLong
import math.integer.uint.factored.UIntP
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.UIntPPI
import math.martix.*
import math.random.nextDoubleNormalDistribution
import math.random.nextIntegerNormalDistribution
import util.stdlib.list
import util.stdlib.mutableList
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/1/26 13:20
 */

//TODO mean must be 0.0
fun Random.samplingContinuousGaussianToDecodingBasis(order: UIntP, mean: Double, sigma: Double): AbstractColumnVector<ComplexNumber<Double>> {
    val x = FieldComplexNumberDouble.columnVector(order.eulerTotient) { FieldDouble.realComplexNumber(nextDoubleNormalDistribution(mean, sigma)) }
    return if (order.value == 2u) {
        x
    } else {
        val ce_ct = CeCTMatrixCreatorComplexDouble.compute(1u, order)
        val cm = ConjugateMirrorMatrixComplexDouble(order.eulerTotient)
        (ce_ct * (cm * x)).columnVectorViewAt(0u)
    }
}

fun Random.samplingContinuousGaussianToDecodingBasis(order: UIntPP, mean: Double, sigma: Double): AbstractColumnVector<ComplexNumber<Double>> {
    val reducePower = order.value / order.prime
    val x = FieldComplexNumberDouble.columnVector(order.eulerTotient) { FieldDouble.realComplexNumber(nextDoubleNormalDistribution(mean, sigma * sqrt(reducePower.toDouble()))) }
    return if (order.prime == 2u) {
        x
    } else {
        val ce_ct = CeCTMatrixCreatorComplexDouble.compute(1u, order.prime())
        val cm = ConjugateMirrorMatrixComplexDouble(order.prime - 1u)
        val matrix = FieldComplexNumberDouble.whiskered(
            order.eulerTotient,
            1u,
            FieldComplexNumberDouble.formalProduct(ce_ct, cm),
            reducePower
        )
        (matrix * x).columnVectorViewAt(0u)
    }
}

//don't know if clearing imaginary part would reduce or cost more error.
fun Random.samplingContinuousGaussianToDecodingBasis(order: UIntPPI, mean: Double, sigma: Double): AbstractColumnVector<ComplexNumber<Double>> =
    when (order) {
        is UIntPP -> samplingContinuousGaussianToDecodingBasis(order, mean, sigma)
        is UIntP  -> samplingContinuousGaussianToDecodingBasis(order, mean, sigma)
        else      -> error("unknown object $order, class: ${order::class}")
    }

//discretization on different base make a difference?

//discretization in decoding basis

/**
 * find a maybe-shortest element on [c] + [p]ℤ^n
 */
fun Random.randomlyShortestElement(c: AbstractColumnVector<Double>, p: UInt): AbstractColumnVector<Double> =
    FieldDouble.columnVector(c.size) { i ->
        randomlySelectNearZero((c.vectorElementAtUnsafe(i) / p.toDouble()).mod(1.0)) * p.toDouble()
    }

/**
 * find a maybe-nearest-to-x element on [c] + [p]ℤ^n
 */
fun Random.randomlyNearestElement(x: AbstractColumnVector<Double>, c: AbstractColumnVector<Double>, p: UInt): AbstractColumnVector<Double> {
    val f = randomlyShortestElement(c.subtract(x), p)
    return x.plus(f)
}

/**
 * find a maybe-nearest-to-x element on [c] + [p]ℤ^n
 */
fun Random.randomlyNearestElement1(x: AbstractColumnVector<Double>, c: AbstractColumnVector<Long>, p: UInt): AbstractColumnVector<Long> {
    require(x.size == c.size)
    return RingLong.columnVector(x.size) { i ->
        val xi = x.vectorElementAtUnsafe(i)
        val ci = c.vectorElementAtUnsafe(i)
        val a = (ci.toDouble() - xi) / p.toDouble()
        val a1 = floor(a)
        val r = a - a1
//        println("a: $a")
//        println("a1: $a1")
//        println("r: $r")
        if (nextDouble() < r) {
            ci - (p.toLong() * (a1.toLong() + 1L))
        } else {
            ci - (p.toLong() * a1.toLong())
        }
    }
}

/**
 * fastest general implementation, but O(n^2), don't use this.
 */
fun Random.approximatelySamplingDiscreteGaussianOnOriginToBasis(size: UInt, sigma: Double, baseJ: AbstractDiagonalMatrix<Double>, baseR: AbstractSquareMatrix<Double>): MutableList<Long> {
    require(size == baseJ.size)
    require(size == baseR.size)
    val x = mutableList(size) { 0L }
    for (i in size - 1u downTo 0u) {
        var rx = 0.0
        for (j in i + 1u until size) {
            rx += baseR.elementAtUnsafe(i, j) * x[j.toInt()]
        }
        val lengthJ = baseJ.vectorElementAtUnsafe(i)
        val sigma1 = sigma / lengthJ
        val sigma1_inv = lengthJ / sigma

        x[i.toInt()] -= nextIntegerNormalDistribution(rx.toBigDecimal(), sigma1.toBigDecimal(), sigma1_inv.toBigDecimal()).longValue(true)
    }
    return x
}

/**
 * cannot guarantee its correctness by test yet.
 */
fun Random.approximatelySamplingDiscreteGaussianOnOriginToPowerBasis(order: UIntP, sigma: Double): List<Long> {
    return if (order.value == 2u) {
        listOf(nextIntegerNormalDistribution(BigDecimal.ZERO, (sigma).toBigDecimal(), (1.0 / sigma).toBigDecimal()).longValue(true))
    } else {
        val p = order.value.toULong()
        val size = order.eulerTotient
//        println("p: $p, size: $size")

        val x = mutableList(size) { 0L }
        var sumX = 0L

        var i = 1u  //run from 1..size
        var xi = size - i   //keep this relation

        val j_xi = sqrt((p * i.toULong()).toDouble() / (i + 1u).toDouble())
        val x_xi = nextIntegerNormalDistribution(BigDecimal.ZERO, (sigma / j_xi).toBigDecimal(), (j_xi / sigma).toBigDecimal()).longValue(true)
        x[xi.toInt()] = x_xi
        sumX += x_xi
        i++
        xi--
//        while (xi >= 0u) {    //bug
        while (i <= size) {
            val j_xi1 = sqrt((p * i.toULong()).toDouble() / (i + 1u).toDouble())
            val x_xi1 = nextIntegerNormalDistribution((sumX.toDouble() / i.toDouble()).toBigDecimal(), (sigma / j_xi1).toBigDecimal(), (j_xi1 / sigma).toBigDecimal()).longValue(true)
            x[xi.toInt()] = x_xi1
            sumX += x_xi1
            i++
            xi--
        }
        x
    }
}

/**
 * cannot guarantee its correctness by test yet.
 */
fun Random.approximatelySamplingDiscreteGaussianOnOriginToPowerBasis(order: UIntPP, sigma: Double): List<Long> {
    return if (order.prime == 2u) {
        val reducePower = order.value / order.prime
        val size = order.eulerTotient
        val J_xi = sqrt(reducePower.toDouble())
        val sigma1 = (sigma / J_xi).toBigDecimal()
        val sigma1_Inv = (J_xi / sigma).toBigDecimal()
        list(size) { nextIntegerNormalDistribution(BigDecimal.ZERO, sigma1, sigma1_Inv).longValue(true) }
    } else {
        val orderL = order.value.toULong()
        val reducePower = order.value / order.prime
        val size = order.eulerTotient

        val x = mutableList(size) { 0L }
        val sumX = mutableList(reducePower) { 0L }

        var i = 1u  //run from 1..p-1
        var xi = size - 1u

        val J_xi = sqrt((orderL * i.toULong()).toDouble() / (i + 1u).toDouble())
        val sigma1 = (sigma / J_xi).toBigDecimal()
        val sigma1_Inv = (J_xi / sigma).toBigDecimal()
        for (j in reducePower - 1u downTo 0u) {
            val x_xi = nextIntegerNormalDistribution(BigDecimal.ZERO, sigma1, sigma1_Inv).longValue(true)
            x[xi.toInt()] = x_xi
            sumX[j.toInt()] += x_xi
            xi--
        }
        i++

        while (i < order.prime) {
            val J_xi_ = sqrt((orderL * i.toULong()).toDouble() / (i + 1u).toDouble())
            val sigma1_ = (sigma / J_xi_).toBigDecimal()
            val sigma1_Inv_ = (J_xi_ / sigma).toBigDecimal()
            for (j in reducePower - 1u downTo 0u) {
                val x_xi = nextIntegerNormalDistribution((sumX[j.toInt()].toDouble() / i.toDouble()).toBigDecimal(), sigma1_, sigma1_Inv_).longValue(true)
                x[xi.toInt()] = x_xi
                sumX[j.toInt()] += x_xi
                xi--
            }
            i++
        }
        x
    }
}

fun Random.approximatelySamplingDiscreteGaussianOnOriginToPowerBasis(order: UIntPPI, sigma: Double): List<Long> {
    return when (order) {
        is UIntPP -> approximatelySamplingDiscreteGaussianOnOriginToPowerBasis(order, sigma)
        is UIntP  -> approximatelySamplingDiscreteGaussianOnOriginToPowerBasis(order, sigma)
        else      -> error("unknown object $order, class: ${order::class}")
    }
}