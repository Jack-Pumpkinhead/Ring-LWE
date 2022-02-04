package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.RootDataUIntPrime
import math.abstract_structure.Ring
import math.integer.operation.modTimes
import math.martix.*
import math.martix.partition.SquareRowBipartiteMatrix

/**
 * Created by CowardlyLion at 2022/1/29 20:56
 */
open class DftMatrixPrime<A>(val root: RootDataUIntPrime<A>) : BackingSquareMatrix<A> {

    final override val ring: Ring<A> get() = root.ring    //or ring would be null in creation of underlyingMatrix

    final override val size: UInt get() = root.order.value

    override fun elementAtUnsafe(row: UInt, column: UInt): A = root.power(modTimes(row, column, size))

    override val underlyingMatrix: AbstractSquareMatrix<A> =
        if (size <= 3u) {
            ring.squareMatrix(size) { i, j ->
                root.power(modTimes(i, j, size))
            }
        } else {
            SquareRowBipartiteMatrix(RowSummationMatrix(ring, size), DftMatrixPrimeLowerPart(root))
        }


    override fun determinant(): A {
        TODO("Not yet implemented")
    }

    override fun hasInverse(): Boolean = ring.hasInverse(ring.ofInteger(size))

    override fun inverse(): AbstractSquareMatrix<A> {
        return ring.formalProduct(DftMatrixPrime(root.conjugate()), ring.scalarMatrix(size, ring.inverse(ring.ofInteger(size))))
    }

}