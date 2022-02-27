package cryptography.lattice.ring_lwe.matrix.canonical_embedding.conjugate_transpose

import cryptography.lattice.ring_lwe.ring.RootUIntPPI

/**
 * Created by CowardlyLion at 2022/2/24 18:22
 */
interface CeMatrixPPICT<A> : CeMatrixPPPICT<A> {

    override val root: RootUIntPPI<A>

}