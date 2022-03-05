package cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose

import cryptography.lattice.ring_lwe.ring.RootPPPI
import math.abstract_structure.Ring
import math.integer.uint.modTimes
import math.martix.AbstractSquareMatrix

/**
 * Created by CowardlyLion at 2022/2/24 18:19
 */
interface CeCTMatrixPPPI<A> : AbstractSquareMatrix<A> {

    val root: RootPPPI<A>

    override val size: UInt get() = root.order.eulerTotient

    override val ring: Ring<A> get() = root.ring

    override fun elementAtUnsafe(row: UInt, column: UInt): A = root.root.cachedInversePower(modTimes(root.order.coprimeNumberAtUnsafe(column), row, root.order.value))

    override fun hasInverse(): Boolean = ring.hasInverse(ring.ofInteger(size))

}