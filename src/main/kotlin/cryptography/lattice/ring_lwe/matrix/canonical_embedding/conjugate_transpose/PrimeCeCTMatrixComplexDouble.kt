package cryptography.lattice.ring_lwe.matrix.canonical_embedding.conjugate_transpose

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.MatrixPaddingZeroFirstRow
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double.PrimeDftMatrixComplexDouble
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import math.complex_number.ComplexNumber
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/2/24 18:12
 */
class PrimeCeCTMatrixComplexDouble(override val root: RootUIntPI<ComplexNumber<Double>>) : CeMatrixPICT<ComplexNumber<Double>> {

    override fun determinant(): ComplexNumber<Double> {
        TODO("Not yet implemented")
    }

    override val inverse: AbstractSquareMatrix<ComplexNumber<Double>>
        get() = TODO("Not yet implemented")

    val dft = PrimeDftMatrixComplexDouble(root.inverse)

    override fun timesImpl(matrix: AbstractMatrix<ComplexNumber<Double>>): AbstractMatrix<ComplexNumber<Double>> {
        return MatrixRemoveLastRow(dft.timesImpl(MatrixPaddingZeroFirstRow(matrix)))
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<ComplexNumber<Double>>): AbstractMatrix<ComplexNumber<Double>> {
        return MatrixRemoveLastRow(dft.timesRowParallelImpl(MatrixPaddingZeroFirstRow(matrix)))
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<ComplexNumber<Double>>, dest: AbstractMutableMatrix<ComplexNumber<Double>>) {
        val result = dft.timesImpl(MatrixPaddingZeroFirstRow(matrix))
        dest.set { i, j ->
            result.elementAtUnsafe(i, j)
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<ComplexNumber<Double>>, dest: AbstractMutableMatrix<ComplexNumber<Double>>) {
        val result = dft.timesRowParallelImpl(MatrixPaddingZeroFirstRow(matrix))
        dest.set { i, j ->
            result.elementAtUnsafe(i, j)
        }
    }
}