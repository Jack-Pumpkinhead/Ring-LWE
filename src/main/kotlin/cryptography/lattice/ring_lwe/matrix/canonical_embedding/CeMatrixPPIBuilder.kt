package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPIBuilder
import cryptography.lattice.ring_lwe.ring.RootUIntP
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import cryptography.lattice.ring_lwe.ring.RootUIntPPI

/**
 * Created by CowardlyLion at 2022/2/13 22:38
 */
interface CeMatrixPPIBuilder<A> {

    val dftBuilder: DftMatrixPPIBuilder<A>

    val cache: MutableMap<RootUIntPI<A>, CeMatrixPI<A>>

    fun buildImpl(root: RootUIntPI<A>): CeMatrixPI<A>

    fun build(root: RootUIntPI<A>) = cache.computeIfAbsent(root) { buildImpl(root) }

    fun build(root: RootUIntPPI<A>): CeMatrixPPI<A> =
        when (root) {
            is RootUIntPP<A> -> {
                ProperPrimePowerCeMatrix(root, build(root.primeSubroot()), dftBuilder.build(root.subrootReducePowerOne()))
            }
            is RootUIntP<A>  -> build(root)
            else             -> error("unknown type of root $root, class: ${root::class}")
        }

}