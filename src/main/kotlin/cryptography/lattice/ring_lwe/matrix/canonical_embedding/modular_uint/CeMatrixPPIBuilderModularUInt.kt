package cryptography.lattice.ring_lwe.matrix.canonical_embedding.modular_uint

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixPI
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixPPIBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPIBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint.DftMatrixPPIBuilderModularUInt
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import math.integer.uint.modular.ModularUInt

/**
 * Created by CowardlyLion at 2022/2/24 16:46
 */
object CeMatrixPPIBuilderModularUInt : CeMatrixPPIBuilder<ModularUInt> {

    override val cache: MutableMap<RootUIntPI<ModularUInt>, CeMatrixPI<ModularUInt>> = mutableMapOf()

    override val dftBuilder: DftMatrixPPIBuilder<ModularUInt> = DftMatrixPPIBuilderModularUInt

    override fun buildImpl(root: RootUIntPI<ModularUInt>): CeMatrixPI<ModularUInt> = PrimeCeMatrixModularUInt(root)

}