package cryptography.lattice.ring_lwe.public_key_cryptosystem

import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.complex_double.CeCTMatrixCreatorComplexDouble
import math.abstract_structure.instance.FieldDouble
import math.complex_number.ComplexNumber
import math.complex_number.FieldComplexNumberDouble
import math.complex_number.realComplexNumber
import math.integer.uint.factored.*
import math.martix.*
import kotlin.math.sqrt

/**
 * Created by CowardlyLion at 2022/4/18 15:47
 */

val random = java.util.Random()
fun samplingContinuousGaussianToDecodingBasisFast(order: UIntP, mean: Double, sigma: Double): AbstractColumnVector<ComplexNumber<Double>> {
    val x = FieldComplexNumberDouble.columnVector(order.eulerTotient) { FieldDouble.realComplexNumber(random.nextGaussian(mean, sigma)) }
    return if (order.value == 2u) {
        x
    } else {
        val ce_ct = CeCTMatrixCreatorComplexDouble.compute(1u, order)
        val cm = ConjugateMirrorMatrixComplexDouble(order.eulerTotient)
        (ce_ct * (cm * x)).columnVectorViewAt(0u)
    }
}

fun samplingContinuousGaussianToDecodingBasisFast(order: UIntPP, mean: Double, sigma: Double): AbstractColumnVector<ComplexNumber<Double>> {
    val reducePower = order.value / order.prime
    val x = FieldComplexNumberDouble.columnVector(order.eulerTotient) { FieldDouble.realComplexNumber(random.nextGaussian(mean, sigma * sqrt(reducePower.toDouble()))) }
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
fun samplingContinuousGaussianToDecodingBasisFast(order: UIntPPI, mean: Double, sigma: Double): AbstractColumnVector<ComplexNumber<Double>> =
    when (order) {
        is UIntPP -> samplingContinuousGaussianToDecodingBasisFast(order, mean, sigma)
        is UIntP  -> samplingContinuousGaussianToDecodingBasisFast(order, mean, sigma)
        else      -> error("unknown object $order, class: ${order::class}")
    }

fun samplingContinuousGaussianToDecodingBasisFast(order: UIntPPP, mean: Double, sigma: Double): AbstractColumnVector<ComplexNumber<Double>> {
    val scale = order.value / order.radical
    val x = FieldComplexNumberDouble.columnVector(order.eulerTotient) { FieldDouble.realComplexNumber(random.nextGaussian(mean, sigma * sqrt(scale.toDouble()))) }
//    val root = RootCalculatorUnsafeComplexNumber.compute(1u, order)   //no need to compute one root then power to subroot (it's all the same)
    val matrices = order.factors.map { factor ->
        if (factor.prime == 2u) {
            FieldComplexNumberDouble.identityMatrix(factor.eulerTotient)
        } else {
            if (factor.power == 1u) {
                val ce_ct = CeCTMatrixCreatorComplexDouble.compute(1u, factor)
                val cm = ConjugateMirrorMatrixComplexDouble(factor.eulerTotient)
                FieldComplexNumberDouble.formalProduct(ce_ct, cm)
            } else {
                val ce_ct = CeCTMatrixCreatorComplexDouble.compute(1u, factor.prime())
                val cm = ConjugateMirrorMatrixComplexDouble(factor.prime - 1u)
                FieldComplexNumberDouble.whiskered(
                    factor.eulerTotient,
                    1u,
                    FieldComplexNumberDouble.formalProduct(ce_ct, cm),
                    factor.value / factor.prime
                )
            }
        }
    }
    return (FieldComplexNumberDouble.formalKroneckerProduct(order.eulerTotient, matrices) * x).columnVectorViewAt(0u)
}

fun samplingContinuousGaussianToDecodingBasisFast(order: UIntPPPI, mean: Double, sigma: Double): AbstractColumnVector<ComplexNumber<Double>> =
    when (order) {
        is UIntPPP -> samplingContinuousGaussianToDecodingBasisFast(order, mean, sigma)
        is UIntPP  -> samplingContinuousGaussianToDecodingBasisFast(order, mean, sigma)
        is UIntP   -> samplingContinuousGaussianToDecodingBasisFast(order, mean, sigma)
        else       -> error("unknown object $order, class: ${order::class}")
    }
