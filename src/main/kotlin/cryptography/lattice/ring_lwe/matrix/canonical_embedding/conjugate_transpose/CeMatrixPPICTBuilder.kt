package cryptography.lattice.ring_lwe.matrix.canonical_embedding.conjugate_transpose

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPIBuilder
import cryptography.lattice.ring_lwe.ring.RootUIntP
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import cryptography.lattice.ring_lwe.ring.RootUIntPPI

/**
 * Created by CowardlyLion at 2022/2/24 19:29
 */
interface CeMatrixPPICTBuilder<A> {

    val dftBuilder: DftMatrixPPIBuilder<A>

    val cache: MutableMap<RootUIntPI<A>, CeMatrixPICT<A>>

    fun buildImpl(root: RootUIntPI<A>): CeMatrixPICT<A>

    fun build(root: RootUIntPI<A>) = cache.computeIfAbsent(root) { buildImpl(root) }

    fun build(root: RootUIntPPI<A>): CeMatrixPPICT<A> =
        when (root) {
            is RootUIntPP<A> -> {
                ProperPrimePowerCeMatrixCT(root, build(root.primeSubroot()), dftBuilder.build(root.subrootReducePowerOne().inverse))
            }
            is RootUIntP<A>  -> build(root)
            else             -> error("unknown type of root $root, class: ${root::class}")
        }

}