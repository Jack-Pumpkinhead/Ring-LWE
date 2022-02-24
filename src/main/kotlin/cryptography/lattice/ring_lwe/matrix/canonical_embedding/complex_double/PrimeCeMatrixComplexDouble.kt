package cryptography.lattice.ring_lwe.matrix.canonical_embedding.complex_double

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixPI
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.MatrixPaddingZeroLastRow
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double.DftMatrixPrimeLowerPartComplexDouble
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import math.complex_number.ComplexNumber
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/2/24 17:16
 */
class PrimeCeMatrixComplexDouble(override val root: RootUIntPI<ComplexNumber<Double>>) : CeMatrixPI<ComplexNumber<Double>> {

    override fun determinant(): ComplexNumber<Double> {
        TODO("Not yet implemented")
    }

    override val inverse: AbstractSquareMatrix<ComplexNumber<Double>>
        get() = TODO("Not yet implemented")

    val threshold = 700u

    val lowerPart: DftMatrixPrimeLowerPartComplexDouble? = if (size <= threshold) null else DftMatrixPrimeLowerPartComplexDouble(root)

    override fun timesImpl(matrix: AbstractMatrix<ComplexNumber<Double>>): AbstractMatrix<ComplexNumber<Double>> =
        if (size <= threshold) {
            super.timesImpl(matrix)
        } else {
            lowerPart!!.timesImpl(MatrixPaddingZeroLastRow(matrix))
        }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<ComplexNumber<Double>>): AbstractMatrix<ComplexNumber<Double>> =
        if (size <= threshold) {
            super.timesRowParallelImpl(matrix)
        } else {
            lowerPart!!.timesRowParallelImpl(MatrixPaddingZeroLastRow(matrix))
        }

    override fun multiplyToImpl(matrix: AbstractMatrix<ComplexNumber<Double>>, dest: AbstractMutableMatrix<ComplexNumber<Double>>) {
        if (size <= threshold) {
            super.multiplyToImpl(matrix, dest)
        } else {
            lowerPart!!.multiplyToImpl(MatrixPaddingZeroLastRow(matrix), dest)
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<ComplexNumber<Double>>, dest: AbstractMutableMatrix<ComplexNumber<Double>>) {
        if (size <= threshold) {
            super.multiplyToRowParallelImpl(matrix, dest)
        } else {
            lowerPart!!.multiplyToRowParallelImpl(MatrixPaddingZeroLastRow(matrix), dest)
        }
    }
}