package cryptography.lattice.ring_lwe.ring

import math.integer.uint.factored.UIntPPP

/**
 * Created by CowardlyLion at 2022/3/3 21:18
 */
class RootPPPInv<A>(override val inverse: RootPPP<A>) : RootPPP<A> {

    override val order: UIntPPP get() = inverse.order

    override val root get() = inverse.root.inverse

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