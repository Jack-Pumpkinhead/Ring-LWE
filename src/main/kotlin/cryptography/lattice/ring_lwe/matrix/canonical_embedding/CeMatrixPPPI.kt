package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.ring.RootUIntPPPI
import math.abstract_structure.Ring
import math.integer.uint.modTimes
import math.martix.AbstractSquareMatrix

/**
 * Created by CowardlyLion at 2022/2/13 21:08
 */
interface CeMatrixPPPI<A> : AbstractSquareMatrix<A> {

    val root: RootUIntPPPI<A>

    override val size: UInt get() = root.order.eulerTotient

    override val ring: Ring<A> get() = root.ring

    override fun elementAtUnsafe(row: UInt, column: UInt): A = root.cachedPower(modTimes(root.order.coprimeNumberAtUnsafe(row), column, root.order.value))

    override fun hasInverse(): Boolean = ring.hasInverse(ring.ofInteger(size))

}