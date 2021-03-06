package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.RootP
import math.abstract_structure.Ring
import math.integer.uint.modTimes
import math.martix.AbstractMatrix

/**
 * Created by CowardlyLion at 2022/1/29 21:17
 *
 * normal multiplication is slightly faster than normal convolution
 */
interface DftMatrixPrimeLowerPart<A> : AbstractMatrix<A> {

    val root: RootP<A>

    override val ring: Ring<A> get() = root.ring
    override val rows: UInt get() = root.order.value - 1u
    override val columns: UInt get() = root.order.value

    override fun elementAtUnsafe(row: UInt, column: UInt): A = root.root.cachedPower(modTimes(row + 1u, column, root.order.value))

}