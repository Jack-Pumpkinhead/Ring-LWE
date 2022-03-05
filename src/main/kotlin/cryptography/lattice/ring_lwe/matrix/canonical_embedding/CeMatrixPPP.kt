package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.ring.RootPPP

/**
 * Created by CowardlyLion at 2022/3/4 23:01
 */
interface CeMatrixPPP<A> : CeMatrixPPPI<A> {

    override val root: RootPPP<A>

}