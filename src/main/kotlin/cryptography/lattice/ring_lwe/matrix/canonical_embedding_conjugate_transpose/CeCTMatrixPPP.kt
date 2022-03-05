package cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose

import cryptography.lattice.ring_lwe.ring.RootPPP

/**
 * Created by CowardlyLion at 2022/3/5 11:21
 */
interface CeCTMatrixPPP<A> : CeCTMatrixPPPI<A> {

    override val root: RootPPP<A>

}