package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.ring.RootUIntPI
import math.martix.AbstractSquareMatrix

/**
 * Created by CowardlyLion at 2022/2/13 22:07
 */
class PrimeCeMatrix<A>(override val root: RootUIntPI<A>) : CeMatrixPI<A> {

    override fun determinant(): A {
        TODO("Not yet implemented")
    }

    override val inverse: AbstractSquareMatrix<A>
        get() = TODO("Not yet implemented")

}