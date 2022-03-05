package cryptography.lattice.ring_lwe.ring

import math.integer.uint.factored.UIntPP

/**
 * Created by CowardlyLion at 2022/3/3 21:08
 */
interface RootPP<A> : RootPPI<A> {

    override val order: UIntPP

    override val inverse: RootPP<A>

}