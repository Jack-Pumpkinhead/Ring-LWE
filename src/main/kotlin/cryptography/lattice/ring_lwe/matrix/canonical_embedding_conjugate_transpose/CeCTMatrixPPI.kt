package cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose

import cryptography.lattice.ring_lwe.ring.RootPPI

/**
 * Created by CowardlyLion at 2022/2/24 18:22
 */
interface CeCTMatrixPPI<A> : CeCTMatrixPPPI<A> {

    override val root: RootPPI<A>

}