package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.ring.RootUIntPI

/**
 * Created by CowardlyLion at 2022/2/13 20:54
 */
interface CeMatrixPI<A> : CeMatrixPPI<A> {

    override val root: RootUIntPI<A>

}