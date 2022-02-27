package cryptography.lattice.ring_lwe.matrix.canonical_embedding.conjugate_transpose

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPIBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double.DftMatrixPPIBuilderComplexDouble
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import math.complex_number.ComplexNumber

/**
 * Created by CowardlyLion at 2022/2/24 19:31
 */
object CeCTMatrixPPIBuilderComplexDouble : CeMatrixPPICTBuilder<ComplexNumber<Double>> {

    override val cache: MutableMap<RootUIntPI<ComplexNumber<Double>>, CeMatrixPICT<ComplexNumber<Double>>> = mutableMapOf()

    override val dftBuilder: DftMatrixPPIBuilder<ComplexNumber<Double>> = DftMatrixPPIBuilderComplexDouble

    override fun buildImpl(root: RootUIntPI<ComplexNumber<Double>>): CeMatrixPICT<ComplexNumber<Double>> = PrimeCeCTMatrixComplexDouble(root)

}