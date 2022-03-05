package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.RootPP

/**
 * Created by CowardlyLion at 2022/3/4 17:32
 */
interface DftMatrixPP<A> : DftMatrixPPI<A> {

    override val root: RootPP<A>

}