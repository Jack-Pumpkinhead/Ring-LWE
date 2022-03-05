package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.default_impl.CeMatrixPPImpl
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixBuilder
import cryptography.lattice.ring_lwe.ring.RootP
import cryptography.lattice.ring_lwe.ring.RootPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import util.mapper.RootToCe

/**
 * Created by CowardlyLion at 2022/3/4 23:17
 */
interface CeMatrixBuilder<A> : RootToCe<A> {

    val dftBuilder: DftMatrixBuilder<A>

    val cacheP: MutableMap<RootP<A>, CeMatrixP<A>>
    val cachePP: MutableMap<RootPP<A>, CeMatrixPP<A>>
    val cachePPP: MutableMap<RootPPP<A>, CeMatrixPPP<A>>

    fun build(a: RootP<A>): CeMatrixP<A>
    fun build(a: RootPP<A>): CeMatrixPP<A> = CeMatrixPPImpl(a, compute(dftBuilder.calculator.subrootPrime(a)), dftBuilder.compute(dftBuilder.calculator.subrootReducePowerOne(a)))
    fun build(a: RootPPP<A>): CeMatrixPPP<A> = TODO("not yet implemented")

    override fun compute(a: RootP<A>): CeMatrixP<A> {
        return if (cacheP.containsKey(a)) {
            cacheP[a]!!
        } else {
            val dft = build(a)
            cacheP[a] = dft
            dft
        }
    }

    override fun compute(a: RootPP<A>): CeMatrixPP<A> {
        return if (cachePP.containsKey(a)) {
            cachePP[a]!!
        } else {
            val dft = build(a)
            cachePP[a] = dft
            dft
        }
    }

    override fun compute(a: RootPPP<A>): CeMatrixPPP<A> {
        return if (cachePPP.containsKey(a)) {
            cachePPP[a]!!
        } else {
            val dft = build(a)
            cachePPP[a] = dft
            dft
        }
    }
}