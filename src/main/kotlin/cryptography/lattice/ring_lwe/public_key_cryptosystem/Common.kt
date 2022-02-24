package cryptography.lattice.ring_lwe.public_key_cryptosystem

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.conjugate_transpose.CeMatrixPPICTBuilderComplexDouble
import math.abstract_structure.instance.FieldComplexNumberDouble
import math.abstract_structure.instance.FieldDouble
import math.complex_number.ComplexNumber
import math.complex_number.realComplexNumber
import math.integer.uint.factored.UIntP
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.UIntPPI
import math.martix.AbstractColumnVector
import math.martix.columnVector
import math.martix.formalProduct
import math.martix.whiskered
import math.random.nextDoubleNormalDistribution
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

fun Random.samplingContinuousGaussianToDecodingBasis(order: UIntPPI, mean: Double, variance: Double): AbstractColumnVector<ComplexNumber<Double>> =
    when (order) {
        is UIntPP -> samplingContinuousGaussianToDecodingBasis(order, mean, variance)
        is UIntP  -> samplingContinuousGaussianToDecodingBasis(order, mean, variance)
        else      -> error("unknown object $order, class: ${order::class}")
    }
