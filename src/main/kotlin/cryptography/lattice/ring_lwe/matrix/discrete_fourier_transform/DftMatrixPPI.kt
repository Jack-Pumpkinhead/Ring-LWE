package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.RootUIntPPI

/**
 * Created by CowardlyLion at 2022/2/13 13:31
 *
 * discrete fourier transform matrix with order in the form of PrimePower
 */
interface DftMatrixPPI<A> : DftMatrixPPPI<A> {

    override val root: RootUIntPPI<A>

}