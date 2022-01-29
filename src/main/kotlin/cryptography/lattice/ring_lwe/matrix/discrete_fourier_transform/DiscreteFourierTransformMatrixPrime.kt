package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.RootDataUIntPrime
import math.abstract_structure.Ring
import math.integer.operation.modTimes
import math.martix.AbstractSquareMatrix
import math.martix.BackingSquareMatrix
import math.martix.partition.SquareRowBipartiteMatrix
import math.martix.squareMatrix

/**
 * Created by CowardlyLion at 2022/1/29 20:56
 */
class DiscreteFourierTransformMatrixPrime<A>(val root: RootDataUIntPrime<A>) : BackingSquareMatrix<A> {

    override val ring: Ring<A> get() = root.ring    //or ring would be null in creation of underlyingMatrix

    override val size: UInt get() = root.order.value

    override fun elementAtUnsafe(row: UInt, column: UInt): A = root.power(modTimes(row, column, size))

    override val underlyingMatrix: AbstractSquareMatrix<A> =
        if (size <= 3u) {
            ring.squareMatrix(size) { i, j ->
                root.power(modTimes(i, j, size))
            }
        } else {
            SquareRowBipartiteMatrix(RowSummationMatrix(ring, size), DiscreteFourierTransformMatrixPrimeLowerPart(root))
        }


    override fun determinant(): A {
        TODO("Not yet implemented")
    }

    override fun hasInverse(): Boolean = true

    override fun inverse(): AbstractSquareMatrix<A> {
        TODO("Not yet implemented")
    }


}