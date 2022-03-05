package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.RootPPP

/**
 * Created by CowardlyLion at 2022/3/4 17:33
 */
interface DftMatrixPPP<A> : DftMatrixPPPI<A> {

    override val root: RootPPP<A>

}