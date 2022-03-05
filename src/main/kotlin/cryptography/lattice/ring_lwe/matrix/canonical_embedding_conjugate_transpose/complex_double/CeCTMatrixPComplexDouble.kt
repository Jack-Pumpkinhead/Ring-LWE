package cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.complex_double

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.MatrixPaddingZeroFirstRow
import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.CeCTMatrixP
import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.MatrixRemoveLastRow
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixP
import cryptography.lattice.ring_lwe.ring.RootP
import math.complex_number.ComplexNumber
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/2/24 18:12
 */
class CeCTMatrixPComplexDouble(override val root: RootP<ComplexNumber<Double>>, val conjugateDft: DftMatrixP<ComplexNumber<Double>>) : CeCTMatrixP<ComplexNumber<Double>> {

    override fun determinant(): ComplexNumber<Double> {
        TODO("Not yet implemented")
    }

    override val inverse: AbstractSquareMatrix<ComplexNumber<Double>>
        get() = TODO("Not yet implemented")

    override fun timesImpl(matrix: AbstractMatrix<ComplexNumber<Double>>): AbstractMatrix<ComplexNumber<Double>> {
        return MatrixRemoveLastRow(conjugateDft.timesImpl(MatrixPaddingZeroFirstRow(matrix)))
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<ComplexNumber<Double>>): AbstractMatrix<ComplexNumber<Double>> {
        return MatrixRemoveLastRow(conjugateDft.timesRowParallelImpl(MatrixPaddingZeroFirstRow(matrix)))
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<ComplexNumber<Double>>, dest: AbstractMutableMatrix<ComplexNumber<Double>>) {
        val result = conjugateDft.timesImpl(MatrixPaddingZeroFirstRow(matrix))
        dest.set { i, j ->
            result.elementAtUnsafe(i, j)
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<ComplexNumber<Double>>, dest: AbstractMutableMatrix<ComplexNumber<Double>>) {
        val result = conjugateDft.timesRowParallelImpl(MatrixPaddingZeroFirstRow(matrix))
        dest.set { i, j ->
            result.elementAtUnsafe(i, j)
        }
    }
}