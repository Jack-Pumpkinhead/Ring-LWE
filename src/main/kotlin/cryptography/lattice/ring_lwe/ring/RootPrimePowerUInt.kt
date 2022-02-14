package cryptography.lattice.ring_lwe.ring

import math.integer.uint.factored.UIntPPI

/**
 * Created by CowardlyLion at 2022/2/12 21:52
 */
interface RootPrimePowerUInt<A> : RootPrimePowerProductUInt<A> {

    override val order: UIntPPI

    override val inverse: RootPrimePowerUInt<A>

}