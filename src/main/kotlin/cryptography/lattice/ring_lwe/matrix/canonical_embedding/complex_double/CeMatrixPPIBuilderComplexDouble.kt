package cryptography.lattice.ring_lwe.matrix.canonical_embedding.complex_double

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixPI
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixPPIBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPIBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double.DftMatrixPPIBuilderComplexDouble
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import math.complex_number.ComplexNumber

/**
 * Created by CowardlyLion at 2022/2/24 17:19
 */
object CeMatrixPPIBuilderComplexDouble : CeMatrixPPIBuilder<ComplexNumber<Double>> {

    override val cache: MutableMap<RootUIntPI<ComplexNumber<Double>>, CeMatrixPI<ComplexNumber<Double>>> = mutableMapOf()

    override val dftBuilder: DftMatrixPPIBuilder<ComplexNumber<Double>> = DftMatrixPPIBuilderComplexDouble

    override fun buildImpl(root: RootUIntPI<ComplexNumber<Double>>): CeMatrixPI<ComplexNumber<Double>> = PrimeCeMatrixComplexDouble(root)

}