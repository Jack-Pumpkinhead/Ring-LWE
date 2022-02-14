package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.RootUIntPPPI
import math.abstract_structure.Ring
import math.integer.uint.modTimes
import math.martix.AbstractSquareMatrix

/**
 * Created by CowardlyLion at 2022/2/13 12:31
 *
 * discrete fourier transform matrix with order in the form of PrimePowerProduct
 */
interface DftMatrixPPPI<A> : AbstractSquareMatrix<A> {

    val root: RootUIntPPPI<A>

    override val size: UInt get() = root.order.value

    override val ring: Ring<A> get() = root.ring

    override fun elementAtUnsafe(row: UInt, column: UInt): A = root.cachedPower(modTimes(row, column, root.order.value))

    override fun hasInverse(): Boolean = ring.hasInverse(ring.ofInteger(size))

}