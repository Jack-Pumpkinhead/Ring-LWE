package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.ring.RootPP

/**
 * Created by CowardlyLion at 2022/3/4 23:00
 */
interface CeMatrixPP<A> : CeMatrixPPI<A> {

    override val root: RootPP<A>

}