package cryptography.lattice.ring_lwe.matrix.canonical_embedding.complex_double

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeInverseMatrixPI
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixPI1
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double.DftMatrixPPIBuilderComplexDouble
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double.DftMatrixPrimeLowerPartComplexDouble
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import math.complex_number.ComplexNumber
import math.martix.AbstractSquareMatrix

/**
 * Created by CowardlyLion at 2022/2/24 17:16
 */
class PrimeCeMatrixComplexDouble(override val root: RootUIntPI<ComplexNumber<Double>>) : CeMatrixPI1<ComplexNumber<Double>> {

    override fun determinant(): ComplexNumber<Double> {
        TODO("Not yet implemented")
    }

    override val inverse: AbstractSquareMatrix<ComplexNumber<Double>> by lazy {
        CeInverseMatrixPI(this, DftMatrixPPIBuilderComplexDouble)
    }

    override val lowerPart: DftMatrixPrimeLowerPartComplexDouble by lazy {
        DftMatrixPrimeLowerPartComplexDouble(root)
    }

    override val thresholdTimes: UInt = 700u    //TODO measure threshold
    override val thresholdTimesRowParallel: UInt = 700u
    override val thresholdMultiplyTo: UInt = 700u
    override val thresholdMultiplyToRowParallel: UInt = 700u

}