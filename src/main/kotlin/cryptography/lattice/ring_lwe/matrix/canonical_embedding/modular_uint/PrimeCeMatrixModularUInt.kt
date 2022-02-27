package cryptography.lattice.ring_lwe.matrix.canonical_embedding.modular_uint

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeInverseMatrixPI
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixPI1
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint.DftMatrixPPIBuilderModularUInt
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint.DftMatrixPrimeLowerPartModularUInt
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import math.integer.uint.modular.ModularUInt
import math.martix.AbstractSquareMatrix

/**
 * Created by CowardlyLion at 2022/2/24 11:51
 */
class PrimeCeMatrixModularUInt(override val root: RootUIntPI<ModularUInt>) : CeMatrixPI1<ModularUInt> {

    override fun determinant(): ModularUInt {
        TODO("Not yet implemented")
    }

    override val inverse: AbstractSquareMatrix<ModularUInt> by lazy {
        CeInverseMatrixPI(this, DftMatrixPPIBuilderModularUInt)
    }

    override val lowerPart: DftMatrixPrimeLowerPartModularUInt by lazy {
        DftMatrixPrimeLowerPartModularUInt(root)
    }

    override val thresholdTimes: UInt = 700u
    override val thresholdTimesRowParallel: UInt = 700u
    override val thresholdMultiplyTo: UInt = 700u
    override val thresholdMultiplyToRowParallel: UInt = 700u

}