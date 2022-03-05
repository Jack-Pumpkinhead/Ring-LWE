package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.ring.RootP

/**
 * Created by CowardlyLion at 2022/3/4 23:01
 */
interface CeMatrixP<A> : CeMatrixPI<A> {

    override val root: RootP<A>

}