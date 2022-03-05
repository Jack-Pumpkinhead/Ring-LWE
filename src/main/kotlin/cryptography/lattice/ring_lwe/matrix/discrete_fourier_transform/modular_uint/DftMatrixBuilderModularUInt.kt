package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPP
import cryptography.lattice.ring_lwe.ring.RootP
import cryptography.lattice.ring_lwe.ring.RootPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafe
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafeModularUInt
import math.integer.uint.modular.ModularUInt

/**
 * Created by CowardlyLion at 2022/3/4 21:12
 */
object DftMatrixBuilderModularUInt : DftMatrixBuilder<ModularUInt> {

    override val calculator: SubrootCalculatorUnsafe<ModularUInt> = SubrootCalculatorUnsafeModularUInt
    override val cacheP: MutableMap<RootP<ModularUInt>, DftMatrixP<ModularUInt>> = mutableMapOf()
    override val cachePP: MutableMap<RootPP<ModularUInt>, DftMatrixPP<ModularUInt>> = mutableMapOf()
    override val cachePPP: MutableMap<RootPPP<ModularUInt>, DftMatrixPPP<ModularUInt>> = mutableMapOf()

    override fun build(a: RootP<ModularUInt>): DftMatrixP<ModularUInt> = DftMatrixPModularUInt(a)

}