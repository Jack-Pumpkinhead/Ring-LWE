package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.RootUIntPI
import math.abstract_structure.Ring
import math.integer.uint.modTimes
import math.martix.*
import math.martix.partition.SquareRowBipartiteMatrix

/**
 * Created by CowardlyLion at 2022/2/13 13:33
 */
class PrimeDftMatrix<A>(override val root: RootUIntPI<A>) : DftMatrixPI<A>, BackingSquareMatrix<A> {

    override fun determinant(): A {
        TODO("Not yet implemented")
    }

    override val size: UInt get() = root.order.value

    override val ring: Ring<A> get() = root.ring

    override val underlyingMatrix: AbstractSquareMatrix<A> =
        if (size <= 3u) {
            ring.squareMatrix(size) { i, j ->
                root.cachedPower(modTimes(i, j, size))
            }
        } else {
            SquareRowBipartiteMatrix(RowSummationMatrix(ring, size), DftMatrixPrimeLowerPart(root))
        }

    override fun hasInverse(): Boolean = ring.hasInverse(ring.ofInteger(size))

    //TODO may have a better inverse method
    override val inverse: AbstractSquareMatrix<A> by lazy {
        ring.formalProduct(ring.scalarMatrix(size, ring.inverse(ring.ofInteger(size))), PrimeDftMatrix(root.inverse))
    }


}