package cryptography.lattice.ring_lwe.matrix

import math.abstract_structure.Ring
import math.integer.PrimePowerUInt

/**
 * Created by CowardlyLion at 2022/1/26 17:29
 */
class RootDataUIntPrimePower<A>(val ring: Ring<A>, val root: A, val order: PrimePowerUInt) {

    fun eulerTotient(): UInt = order.eulerTotient()

}