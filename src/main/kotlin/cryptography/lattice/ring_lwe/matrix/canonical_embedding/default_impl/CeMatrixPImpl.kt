package cryptography.lattice.ring_lwe.matrix.canonical_embedding.default_impl

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixP
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.MatrixPaddingZeroLastRow
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPrimeLowerPart
import math.martix.AbstractMatrix
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/2/13 22:07
 */
interface CeMatrixPImpl<A> : CeMatrixP<A> {

    val thresholdTimes: UInt
    val thresholdTimesRowParallel: UInt
    val thresholdMultiplyTo: UInt
    val thresholdMultiplyToRowParallel: UInt

    val lowerPart: DftMatrixPrimeLowerPart<A>   //use by lazy{}


    //TODO decide using underlying matrix or direct multiplication by elementAtUnsafe()
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