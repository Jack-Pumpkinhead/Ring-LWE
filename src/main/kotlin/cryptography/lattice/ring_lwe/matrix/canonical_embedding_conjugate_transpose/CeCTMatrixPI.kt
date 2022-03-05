package cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose

import cryptography.lattice.ring_lwe.ring.RootPI

/**
 * Created by CowardlyLion at 2022/2/24 18:23
 */
interface CeCTMatrixPI<A> : CeCTMatrixPPI<A> {

    override val root: RootPI<A>

}