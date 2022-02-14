package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.RootUIntPI
import cryptography.lattice.ring_lwe.ring.RootUIntPPI

/**
 * Created by CowardlyLion at 2022/2/13 16:12
 */
class DftMatrixPPIBuilderImpl<A> : DftMatrixPPIBuilder<A> {

    override val cache = mutableMapOf<RootUIntPPI<A>, DftMatrixPPI<A>>()

    override fun buildImpl(root: RootUIntPI<A>): DftMatrixPI<A> = DftMatrixP(root)

}