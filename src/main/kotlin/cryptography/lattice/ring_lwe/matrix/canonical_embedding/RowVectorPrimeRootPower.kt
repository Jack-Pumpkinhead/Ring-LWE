package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.ring.RootP
import math.abstract_structure.Ring
import math.martix.AbstractRowVector

/**
 * Created by CowardlyLion at 2022/2/27 20:27
 */
class RowVectorPrimeRootPower<A>(val root: RootP<A>) : AbstractRowVector<A> {

    override val ring: Ring<A> get() = root.ring
    override val size: UInt get() = root.order.eulerTotient

    override fun vectorElementAtUnsafe(index: UInt): A = root.root.cachedPower(index + 1u)

}