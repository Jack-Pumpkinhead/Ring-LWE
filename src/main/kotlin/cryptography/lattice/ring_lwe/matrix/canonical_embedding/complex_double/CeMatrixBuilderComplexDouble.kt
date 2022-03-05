package cryptography.lattice.ring_lwe.matrix.canonical_embedding.complex_double

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixBuilder
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixP
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixPP
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixPPP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double.DftMatrixBuilderComplexDouble
import cryptography.lattice.ring_lwe.ring.RootP
import cryptography.lattice.ring_lwe.ring.RootPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import math.complex_number.ComplexNumber

/**
 * Created by CowardlyLion at 2022/3/4 23:29
 */
object CeMatrixBuilderComplexDouble : CeMatrixBuilder<ComplexNumber<Double>> {

    override val dftBuilder: DftMatrixBuilder<ComplexNumber<Double>> = DftMatrixBuilderComplexDouble

    override val cacheP: MutableMap<RootP<ComplexNumber<Double>>, CeMatrixP<ComplexNumber<Double>>> = mutableMapOf()
    override val cachePP: MutableMap<RootPP<ComplexNumber<Double>>, CeMatrixPP<ComplexNumber<Double>>> = mutableMapOf()
    override val cachePPP: MutableMap<RootPPP<ComplexNumber<Double>>, CeMatrixPPP<ComplexNumber<Double>>> = mutableMapOf()

    override fun build(a: RootP<ComplexNumber<Double>>): CeMatrixP<ComplexNumber<Double>> = CeMatrixPComplexDouble(a)

}