package cryptography.lattice.ring_lwe.matrix.canonical_embedding.modular_uint

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixPI
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeRowPaddingZero
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint.DftMatrixPrimeLowerPartModularUInt
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import math.integer.uint.modular.ModularUInt
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/2/24 11:51
 */
class PrimeCeMatrixModularUInt(override val root: RootUIntPI<ModularUInt>) : CeMatrixPI<ModularUInt> {

    override fun determinant(): ModularUInt {
        TODO("Not yet implemented")
    }

    override val inverse: AbstractSquareMatrix<ModularUInt>
        get() = TODO("Not yet implemented")

    val threshold = 700u

    val lowerPart: DftMatrixPrimeLowerPartModularUInt? = if (size <= threshold) null else DftMatrixPrimeLowerPartModularUInt(root)

    override fun timesImpl(matrix: AbstractMatrix<ModularUInt>): AbstractMatrix<ModularUInt> =
        if (size <= threshold) {
            super.timesImpl(matrix)
        } else {
            lowerPart!!.timesImpl(CeRowPaddingZero(matrix))
        }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<ModularUInt>): AbstractMatrix<ModularUInt> =
        if (size <= threshold) {
            super.timesRowParallelImpl(matrix)
        } else {
            lowerPart!!.timesRowParallelImpl(CeRowPaddingZero(matrix))
        }

    override fun multiplyToImpl(matrix: AbstractMatrix<ModularUInt>, dest: AbstractMutableMatrix<ModularUInt>) {
        if (size <= threshold) {
            super.multiplyToImpl(matrix, dest)
        } else {
            lowerPart!!.multiplyToImpl(CeRowPaddingZero(matrix), dest)
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<ModularUInt>, dest: AbstractMutableMatrix<ModularUInt>) {
        if (size <= threshold) {
            super.multiplyToRowParallelImpl(matrix, dest)
        } else {
            lowerPart!!.multiplyToRowParallelImpl(CeRowPaddingZero(matrix), dest)
        }
    }
}