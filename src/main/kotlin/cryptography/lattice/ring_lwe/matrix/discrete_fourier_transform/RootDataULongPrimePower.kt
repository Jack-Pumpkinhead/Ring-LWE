package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import math.abstract_structure.Ring
import math.integer.PrimePowerULong

/**
 * Created by CowardlyLion at 2022/1/26 16:42
 */
class RootDataULongPrimePower<A>(val ring: Ring<A>, val root: A, val order: PrimePowerULong) {

    fun eulerTotient(): ULong = order.eulerTotient()

}