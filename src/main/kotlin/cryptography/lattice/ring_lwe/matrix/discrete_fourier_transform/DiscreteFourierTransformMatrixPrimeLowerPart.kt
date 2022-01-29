package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.RootDataUIntPrime
import math.abstract_structure.Ring
import math.integer.operation.modTimes
import math.martix.AbstractMatrix
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/29 21:17
 */
class DiscreteFourierTransformMatrixPrimeLowerPart<A>(val root: RootDataUIntPrime<A>) : AbstractMatrix<A> {

    override val ring: Ring<A> get() = root.ring

    override val rows: UInt get() = root.order.value - 1u

    override val columns: UInt get() = root.order.value

    override fun elementAtUnsafe(row: UInt, column: UInt): A = ring.powerM(root.root, modTimes(row + 1u, column, root.order.value))


}