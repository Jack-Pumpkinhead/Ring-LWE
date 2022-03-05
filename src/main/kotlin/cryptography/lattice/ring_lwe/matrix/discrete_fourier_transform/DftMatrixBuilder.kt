package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.default_impl.DftMatrixPImpl
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.default_impl.DftMatrixPPImpl
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.default_impl.DftMatrixPPPImpl
import cryptography.lattice.ring_lwe.ring.RootP
import cryptography.lattice.ring_lwe.ring.RootPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafe
import util.errorUnknownObject
import util.mapper.RootToDft

/**
 * Created by CowardlyLion at 2022/3/4 18:08
 */
interface DftMatrixBuilder<A> : RootToDft<A> {

    val calculator: SubrootCalculatorUnsafe<A>

    val cacheP: MutableMap<RootP<A>, DftMatrixP<A>>
    val cachePP: MutableMap<RootPP<A>, DftMatrixPP<A>>
    val cachePPP: MutableMap<RootPPP<A>, DftMatrixPPP<A>>

    //use 'apply' to caching result
    fun build(a: RootP<A>): DftMatrixP<A> = DftMatrixPImpl(a)
    fun build(a: RootPP<A>): DftMatrixPP<A> =
        when (val subroot = calculator.subrootReducePowerOne(a)) {
            is RootPP -> {
                val primeCase = compute(calculator.subrootPrime(a))
                val decreaseCase = compute(subroot)
                DftMatrixPPImpl(a, primeCase, decreaseCase)
            }
            is RootP  -> {
                val primeCase = compute(subroot)
                DftMatrixPPImpl(a, primeCase, primeCase)
            }
            else      -> errorUnknownObject(subroot)
        }

    fun build(a: RootPPP<A>): DftMatrixPPP<A> = DftMatrixPPPImpl(a, calculator.allMaximalPrimePowerSubroot(a).map { compute(it) })

    //cannot use 'computeIfAbsent' since there are other map-modifying operation during build process.
    override fun compute(a: RootP<A>): DftMatrixP<A> {
        return if (cacheP.containsKey(a)) {
            cacheP[a]!!
        } else {
            val dft = build(a)
            cacheP[a] = dft
            dft
        }
    }

    override fun compute(a: RootPP<A>): DftMatrixPP<A> {
        return if (cachePP.containsKey(a)) {
            cachePP[a]!!
        } else {
            val dft = build(a)
            cachePP[a] = dft
            dft
        }
    }

    override fun compute(a: RootPPP<A>): DftMatrixPPP<A> {
        return if (cachePPP.containsKey(a)) {
            cachePPP[a]!!
        } else {
            val dft = build(a)
            cachePPP[a] = dft
            dft
        }
    }

}