package cryptography.lattice.ring_lwe.public_key_cryptosystem

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.conjugate_transpose.CeMatrixPPICTBuilderComplexDouble
import cryptography.lattice.ring_lwe.randomlySelectNearZero
import math.abstract_structure.instance.FieldComplexNumberDouble
import math.abstract_structure.instance.FieldDouble
import math.complex_number.ComplexNumber
import math.complex_number.realComplexNumber
import math.integer.long.RingLong
import math.integer.uint.factored.UIntP
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.UIntPPI
import math.martix.AbstractColumnVector
import math.martix.columnVector
import math.martix.formalProduct
import math.martix.whiskered
import math.random.nextDoubleNormalDistribution
import kotlin.math.floor
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/1/26 13:20
 */

fun Random.samplingContinuousGaussianToDecodingBasis(order: UIntP, mean: Double, variance: Double): AbstractColumnVector<ComplexNumber<Double>> {
    val x = FieldComplexNumberDouble.columnVector(order.eulerTotient) { FieldDouble.realComplexNumber(nextDoubleNormalDistribution(mean, variance)) }
    return if (order.value == 2u) {
        x
    } else {
        val ce_ct = CeMatrixPPICTBuilderComplexDouble.build(FieldComplexNumberDouble.root(order))
        val cm = ConjugateMirrorMatrixComplexDouble(order.eulerTotient)
        (ce_ct * (cm * x)).columnVectorViewAt(0u)
    }
}

fun Random.samplingContinuousGaussianToDecodingBasis(order: UIntPP, mean: Double, variance: Double): AbstractColumnVector<ComplexNumber<Double>> {
    val x = FieldComplexNumberDouble.columnVector(order.eulerTotient) { FieldDouble.realComplexNumber(nextDoubleNormalDistribution(mean, variance)) }
    return if (order.prime == 2u) {
        x
    } else {
        val ce_ct = CeMatrixPPICTBuilderComplexDouble.build(FieldComplexNumberDouble.root(UIntP(order.prime)))
        val cm = ConjugateMirrorMatrixComplexDouble(order.prime - 1u)
        val matrix = FieldComplexNumberDouble.whiskered(
            order.eulerTotient,
            1u,
            FieldComplexNumberDouble.formalProduct(ce_ct, cm),
            order.value / order.prime
        )
        (matrix * x).columnVectorViewAt(0u)
    }
}

//don't know if clearing imaginary part would reduce or cost more error.
fun Random.samplingContinuousGaussianToDecodingBasis(order: UIntPPI, mean: Double, variance: Double): AbstractColumnVector<ComplexNumber<Double>> =
    when (order) {
        is UIntPP -> samplingContinuousGaussianToDecodingBasis(order, mean, variance)
        is UIntP  -> samplingContinuousGaussianToDecodingBasis(order, mean, variance)
        else      -> error("unknown object $order, class: ${order::class}")
    }

//discretization on different base make a difference?

//discretization in decoding basis

/**
 * find a maybe-shortest element on [c] + [p]ℤ
 */
fun Random.randomlyShortestElement(c: AbstractColumnVector<Double>, p: UInt): AbstractColumnVector<Double> =
    FieldDouble.columnVector(c.size) { i ->
        randomlySelectNearZero((c.vectorElementAtUnsafe(i) / p.toDouble()).mod(1.0)) * p.toDouble()
    }

/**
 * find a maybe-nearest element on [c] + [p]Z to [x]
 */
fun Random.randomlyNearestElement(x: AbstractColumnVector<Double>, c: AbstractColumnVector<Double>, p: UInt): AbstractColumnVector<Double> {
    val f = randomlyShortestElement(c.subtract(x), p)
    return x.plus(f)
}

/**
 * find a maybe-nearest element on [c] + [p]Z to [x]
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

