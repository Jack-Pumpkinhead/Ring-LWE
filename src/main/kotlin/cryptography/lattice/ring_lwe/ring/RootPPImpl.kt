package cryptography.lattice.ring_lwe.ring

import math.abstract_structure.Ring
import math.abstract_structure.monoid.MonoidElementCachePower
import math.integer.uint.factored.UIntPP
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/3/3 20:56
 */
class RootPPImpl<A>(override val ring: Ring<A>, override val root: MonoidElementCachePower<A>, override val order: UIntPP) : RootPP<A> {

    init {
        lazyAssert2 {
            assert(root.order == order.value)
            assert(ring == root.monoid)
        }
    }

    override val inverse get() = RootPPInv(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RootPPPI<*>) return false
        if (root != other.root) return false
        return true
    }

    override fun hashCode(): Int {
        return root.hashCode()
    }

    override fun toString(): String = root.toString()

}