package cryptography.lattice.ring_lwe.matrix.canonical_embedding.modular_uint

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixBuilder
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixP
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixPP
import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixPPP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint.DftMatrixBuilderModularUInt
import cryptography.lattice.ring_lwe.ring.RootP
import cryptography.lattice.ring_lwe.ring.RootPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import math.integer.uint.modular.ModularUInt

/**
 * Created by CowardlyLion at 2022/3/4 23:36
 */
object CeMatrixBuilderModularUInt : CeMatrixBuilder<ModularUInt> {

    override val dftBuilder: DftMatrixBuilder<ModularUInt> = DftMatrixBuilderModularUInt

    override val cacheP: MutableMap<RootP<ModularUInt>, CeMatrixP<ModularUInt>> = mutableMapOf()
    override val cachePP: MutableMap<RootPP<ModularUInt>, CeMatrixPP<ModularUInt>> = mutableMapOf()
    override val cachePPP: MutableMap<RootPPP<ModularUInt>, CeMatrixPPP<ModularUInt>> = mutableMapOf()

    override fun build(a: RootP<ModularUInt>): CeMatrixP<ModularUInt> = CeMatrixPModularUInt(a)

}