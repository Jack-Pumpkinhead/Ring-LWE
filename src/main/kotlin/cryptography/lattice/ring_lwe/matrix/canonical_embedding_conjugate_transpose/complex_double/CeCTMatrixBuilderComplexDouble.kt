package cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.complex_double

import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.CeCTMatrixBuilder
import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.CeCTMatrixP
import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.CeCTMatrixPP
import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.CeCTMatrixPPP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double.DftMatrixBuilderComplexDouble
import cryptography.lattice.ring_lwe.ring.RootP
import cryptography.lattice.ring_lwe.ring.RootPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import math.complex_number.ComplexNumber

/**
 * Created by CowardlyLion at 2022/3/5 11:43
 */
object CeCTMatrixBuilderComplexDouble : CeCTMatrixBuilder<ComplexNumber<Double>> {

    override val dftBuilder: DftMatrixBuilder<ComplexNumber<Double>> = DftMatrixBuilderComplexDouble

    override val cacheP: MutableMap<RootP<ComplexNumber<Double>>, CeCTMatrixP<ComplexNumber<Double>>> = mutableMapOf()
    override val cachePP: MutableMap<RootPP<ComplexNumber<Double>>, CeCTMatrixPP<ComplexNumber<Double>>> = mutableMapOf()
    override val cachePPP: MutableMap<RootPPP<ComplexNumber<Double>>, CeCTMatrixPPP<ComplexNumber<Double>>> = mutableMapOf()

    override fun build(a: RootP<ComplexNumber<Double>>) = CeCTMatrixPComplexDouble(a, dftBuilder.compute(a.inverse))

}