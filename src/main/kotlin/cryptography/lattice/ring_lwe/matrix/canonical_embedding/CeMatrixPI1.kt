package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPrimeLowerPart
import math.martix.AbstractMatrix
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/2/13 22:07
 */
interface CeMatrixPI1<A> : CeMatrixPI<A> {

    val thresholdTimes: UInt
    val thresholdTimesRowParallel: UInt
    val thresholdMultiplyTo: UInt
    val thresholdMultiplyToRowParallel: UInt

    val lowerPart: DftMatrixPrimeLowerPart<A>   //use by lazy{}


    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> =
        if (size <= thresholdTimes) {
            super.timesImpl(matrix)
        } else {
            lowerPart.timesImpl(MatrixPaddingZeroLastRow(matrix))
        }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> =
        if (size <= thresholdTimesRowParallel) {
            super.timesRowParallelImpl(matrix)
        } else {
            lowerPart.timesRowParallelImpl(MatrixPaddingZeroLastRow(matrix))
        }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        if (size <= thresholdMultiplyTo) {
            super.multiplyToImpl(matrix, dest)
        } else {
            lowerPart.multiplyToImpl(MatrixPaddingZeroLastRow(matrix), dest)
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        if (size <= thresholdMultiplyToRowParallel) {
            super.multiplyToRowParallelImpl(matrix, dest)
        } else {
            lowerPart.multiplyToRowParallelImpl(MatrixPaddingZeroLastRow(matrix), dest)
        }
    }
}