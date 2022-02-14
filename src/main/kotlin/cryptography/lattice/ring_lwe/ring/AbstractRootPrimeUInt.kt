package cryptography.lattice.ring_lwe.ring

import math.integer.uint.factored.UIntPI

/**
 * Created by CowardlyLion at 2022/2/13 21:50
 */
interface AbstractRootPrimeUInt<A> : RootPrimePowerUInt<A> {

    override val order: UIntPI

    override val inverse: AbstractRootPrimeUInt<A>

}