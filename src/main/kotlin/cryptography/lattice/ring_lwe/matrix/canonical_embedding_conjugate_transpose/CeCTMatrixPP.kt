package cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose

import cryptography.lattice.ring_lwe.ring.RootPP

/**
 * Created by CowardlyLion at 2022/3/5 11:29
 */
interface CeCTMatrixPP<A> : CeCTMatrixPPI<A> {

    override val root: RootPP<A>

}