package cryptography.lattice.ring_lwe.ring

import math.integer.uint.factored.UIntPPP

/**
 * Created by CowardlyLion at 2022/3/3 21:08
 */
interface RootPPP<A> : RootPPPI<A> {

    override val order: UIntPPP

    override val inverse: RootPPP<A>

}