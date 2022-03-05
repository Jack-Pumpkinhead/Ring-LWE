package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.default_impl

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPP
import cryptography.lattice.ring_lwe.ring.RootP
import cryptography.lattice.ring_lwe.ring.RootPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafe
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafeImpl

/**
 * Created by CowardlyLion at 2022/3/4 21:10
 */
class DftMatrixBuilderImpl<A> : DftMatrixBuilder<A> {

    override val calculator: SubrootCalculatorUnsafe<A> = SubrootCalculatorUnsafeImpl()

    override val cacheP: MutableMap<RootP<A>, DftMatrixP<A>> = mutableMapOf()
    override val cachePP: MutableMap<RootPP<A>, DftMatrixPP<A>> = mutableMapOf()
    override val cachePPP: MutableMap<RootPPP<A>, DftMatrixPPP<A>> = mutableMapOf()

}