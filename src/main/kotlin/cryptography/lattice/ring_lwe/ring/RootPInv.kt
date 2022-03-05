package cryptography.lattice.ring_lwe.ring

import math.abstract_structure.Ring
import math.integer.uint.factored.UIntP

/**
 * Created by CowardlyLion at 2022/3/3 21:09
 */
class RootPInv<A>(override val inverse: RootP<A>) : RootP<A> {

    override val order: UIntP get() = inverse.order

    override val ring: Ring<A> get() = inverse.ring

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

    override fun toString(): String = root.toString()

}