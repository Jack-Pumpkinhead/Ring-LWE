package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.ring.RootPPI

/**
 * Created by CowardlyLion at 2022/2/13 21:48
 */
interface CeMatrixPPI<A> : CeMatrixPPPI<A> {

    override val root: RootPPI<A>

}