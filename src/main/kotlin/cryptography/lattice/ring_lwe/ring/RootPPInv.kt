package cryptography.lattice.ring_lwe.ring

import math.integer.uint.factored.UIntPP

/**
 * Created by CowardlyLion at 2022/3/3 21:13
 */
class RootPPInv<A>(override val inverse: RootPP<A>) : RootPP<A> {

    override val order: UIntPP get() = inverse.order

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