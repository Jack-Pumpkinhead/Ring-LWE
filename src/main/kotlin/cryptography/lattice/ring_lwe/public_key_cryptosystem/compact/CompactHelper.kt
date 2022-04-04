package cryptography.lattice.ring_lwe.public_key_cryptosystem.compact

import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.complex_double.CeCTMatrixCreatorComplexDouble
import cryptography.lattice.ring_lwe.public_key_cryptosystem.ConjugateMirrorMatrixComplexDouble
import cryptography.lattice.ring_lwe.public_key_cryptosystem.samplingContinuousGaussianToDecodingBasis
import math.abstract_structure.instance.FieldDouble
import math.complex_number.ComplexNumber
import math.complex_number.FieldComplexNumberDouble
import math.complex_number.realComplexNumber
import math.integer.uint.factored.UIntP
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.UIntPPP
import math.integer.uint.factored.UIntPPPI
import math.martix.*
import math.random.nextDoubleNormalDistribution
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/4/4 19:54
 */
fun Random.samplingContinuousGaussianToDecodingBasis(order: UIntPPP, mean: Double, sigma: Double): AbstractColumnVector<ComplexNumber<Double>> {
    val scale = order.value / order.radical
    val x = FieldComplexNumberDouble.columnVector(order.eulerTotient) { FieldDouble.realComplexNumber(nextDoubleNormalDistribution(mean, sigma * sqrt(scale.toDouble()))) }
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

fun Random.samplingContinuousGaussianToDecodingBasis(order: UIntPPPI, mean: Double, sigma: Double): AbstractColumnVector<ComplexNumber<Double>> =
    when (order) {
        is UIntPPP -> samplingContinuousGaussianToDecodingBasis(order, mean, sigma)
        is UIntPP  -> samplingContinuousGaussianToDecodingBasis(order, mean, sigma)
        is UIntP   -> samplingContinuousGaussianToDecodingBasis(order, mean, sigma)
        else       -> error("unknown object $order, class: ${order::class}")
    }
