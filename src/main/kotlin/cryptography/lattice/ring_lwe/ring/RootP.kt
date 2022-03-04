package cryptography.lattice.ring_lwe.ring

import math.integer.uint.factored.UIntP

/**
 * Created by CowardlyLion at 2022/3/3 21:07
 */
interface RootP<A> : RootPI<A> {

    override val order: UIntP

    override val inverse: RootP<A>

}