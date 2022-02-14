package cryptography.lattice.ring_lwe.ring

import math.abstract_structure.Ring
import math.integer.uint.factored.UIntPPPI

/**
 * Created by CowardlyLion at 2022/2/12 21:50
 */
interface RootPrimePowerProductUInt<A> {

    val ring: Ring<A>

    val root: A

    val order: UIntPPPI

    fun cachedPower(exponent: UInt): A

    fun cachedInversePower(exponent: UInt): A

    val inverse: RootPrimePowerProductUInt<A>

}