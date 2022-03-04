package cryptography.lattice.ring_lwe.ring

import math.integer.uint.factored.UIntPI

/**
 * Created by CowardlyLion at 2022/3/3 20:54
 */
interface RootPI<A> : RootPPI<A> {

    override val order: UIntPI

    override val inverse: RootPI<A>

}