package cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose

import cryptography.lattice.ring_lwe.ring.RootP

/**
 * Created by CowardlyLion at 2022/3/5 11:30
 */
interface CeCTMatrixP<A> : CeCTMatrixPI<A> {

    override val root: RootP<A>

}