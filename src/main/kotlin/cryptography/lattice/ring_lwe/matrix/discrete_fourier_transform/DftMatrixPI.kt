package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.RootUIntPI

/**
 * Created by CowardlyLion at 2022/2/13 17:41
 *
 * discrete fourier transform matrix with order in the form of Prime
 */
interface DftMatrixPI<A> : DftMatrixPPI<A> {

    override val root: RootUIntPI<A>

}