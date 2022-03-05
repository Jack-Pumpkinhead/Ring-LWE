package cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose

import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.default_impl.CeCTMatrixPPImpl
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixBuilder
import cryptography.lattice.ring_lwe.ring.RootP
import cryptography.lattice.ring_lwe.ring.RootPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import util.mapper.RootToCeCT

/**
 * Created by CowardlyLion at 2022/3/5 11:35
 */
interface CeCTMatrixBuilder<A> : RootToCeCT<A> {

    val dftBuilder: DftMatrixBuilder<A>

    val cacheP: MutableMap<RootP<A>, CeCTMatrixP<A>>
    val cachePP: MutableMap<RootPP<A>, CeCTMatrixPP<A>>
    val cachePPP: MutableMap<RootPPP<A>, CeCTMatrixPPP<A>>

    fun build(a: RootP<A>): CeCTMatrixP<A>
    fun build(a: RootPP<A>): CeCTMatrixPP<A> = CeCTMatrixPPImpl(a, compute(dftBuilder.calculator.subrootPrime(a)), dftBuilder.compute(dftBuilder.calculator.subrootReducePowerOne(a).inverse))
    fun build(a: RootPPP<A>): CeCTMatrixPPP<A> = TODO("not yet implemented")

    override fun compute(a: RootP<A>): CeCTMatrixP<A> {
        return if (cacheP.containsKey(a)) {
            cacheP[a]!!
        } else {
            val dft = build(a)
            cacheP[a] = dft
            dft
        }
    }

    override fun compute(a: RootPP<A>): CeCTMatrixPP<A> {
        return if (cachePP.containsKey(a)) {
            cachePP[a]!!
        } else {
            val dft = build(a)
            cachePP[a] = dft
            dft
        }
    }

    override fun compute(a: RootPPP<A>): CeCTMatrixPPP<A> {
        return if (cachePPP.containsKey(a)) {
            cachePPP[a]!!
        } else {
            val dft = build(a)
            cachePPP[a] = dft
            dft
        }
    }
}