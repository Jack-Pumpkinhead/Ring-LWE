package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.RootP

/**
 * Created by CowardlyLion at 2022/3/4 17:31
 */
interface DftMatrixP<A> : DftMatrixPI<A> {

    override val root: RootP<A>

}