package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.default_impl

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixP
import cryptography.lattice.ring_lwe.ring.RootP
import math.martix.AbstractSquareMatrix
import math.martix.formalProduct
import math.martix.scalarMatrix

/**
 * Created by CowardlyLion at 2022/2/13 13:33
 */
class DftMatrixPImpl<A>(override val root: RootP<A>) : DftMatrixP<A> {

    override fun determinant(): A {
        TODO("Not yet implemented")
    }

//    override val size: UInt get() = root.order.value

//    override val ring: Ring<A> get() = root.ring

    /*override val underlyingMatrix: AbstractSquareMatrix<A> =
        if (size <= 3u) {
            ring.squareMatrix(size) { i, j ->
                root.cachedPower(modTimes(i, j, size))
            }
        } else {
            SquareRowBipartiteMatrix(RowSummationMatrix(ring, size), DftMatrixPrimeLowerPart(root))
        }*/

//    override fun hasInverse(): Boolean = ring.hasInverse(ring.ofInteger(size))

    //TODO may have a better inverse method
    //TODO call builder to create dft
    override val inverse: AbstractSquareMatrix<A> by lazy {
        ring.formalProduct(ring.scalarMatrix(size, ring.inverse(ring.ofInteger(size))), DftMatrixPImpl(root.inverse))
    }


}