package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPP
import cryptography.lattice.ring_lwe.ring.RootP
import cryptography.lattice.ring_lwe.ring.RootPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafe
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafeComplexDouble
import math.complex_number.ComplexNumber

/**
 * Created by CowardlyLion at 2022/3/4 21:50
 */
object DftMatrixBuilderComplexDouble : DftMatrixBuilder<ComplexNumber<Double>> {

    override val calculator: SubrootCalculatorUnsafe<ComplexNumber<Double>> = SubrootCalculatorUnsafeComplexDouble

    //equality in RootP using CyclotomicNumber's
    override val cacheP: MutableMap<RootP<ComplexNumber<Double>>, DftMatrixP<ComplexNumber<Double>>> = mutableMapOf()
    override val cachePP: MutableMap<RootPP<ComplexNumber<Double>>, DftMatrixPP<ComplexNumber<Double>>> = mutableMapOf()
    override val cachePPP: MutableMap<RootPPP<ComplexNumber<Double>>, DftMatrixPPP<ComplexNumber<Double>>> = mutableMapOf()

    override fun build(a: RootP<ComplexNumber<Double>>): DftMatrixP<ComplexNumber<Double>> = DftMatrixPComplexDouble(a)

}