package cryptography.lattice.ring_lwe.ring

import math.integer.uint.factored.UIntPPI

/**
 * Created by CowardlyLion at 2022/3/3 20:53
 */
interface RootPPI<A> : RootPPPI<A> {

    override val order: UIntPPI

    override val inverse: RootPPI<A>

}