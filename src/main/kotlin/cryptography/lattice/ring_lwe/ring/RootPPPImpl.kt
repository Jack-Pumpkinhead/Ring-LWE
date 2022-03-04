package cryptography.lattice.ring_lwe.ring

import math.abstract_structure.monoid.MonoidElementCachePower
import math.integer.uint.factored.UIntPPP
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/3/3 20:57
 */
class RootPPPImpl<A>(override val root: MonoidElementCachePower<A>, override val order: UIntPPP) : RootPPP<A> {

    init {
        lazyAssert2 {
            assert(root.order == order.value)
        }
    }

    override val inverse get() = RootPPPInv(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RootPPPI<*>) return false
        if (root != other.root) return false
        return true
    }

    override fun hashCode(): Int {
        return root.hashCode()
    }

}