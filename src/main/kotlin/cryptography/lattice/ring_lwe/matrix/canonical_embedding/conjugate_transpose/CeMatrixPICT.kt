package cryptography.lattice.ring_lwe.matrix.canonical_embedding.conjugate_transpose

import cryptography.lattice.ring_lwe.ring.RootUIntPI

/**
 * Created by CowardlyLion at 2022/2/24 18:23
 */
interface CeMatrixPICT<A> : CeMatrixPPICT<A> {

    override val root: RootUIntPI<A>

}