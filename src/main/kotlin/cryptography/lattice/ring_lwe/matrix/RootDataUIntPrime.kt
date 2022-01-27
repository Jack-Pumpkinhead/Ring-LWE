package cryptography.lattice.ring_lwe.matrix

import math.abstract_structure.Ring
import math.integer.FactorizationUIntPrime

/**
 * Created by CowardlyLion at 2022/1/26 17:28
 *
 * [order] need to be prime
 */
class RootDataUIntPrime<A>(val ring: Ring<A>, val root: A, val order: FactorizationUIntPrime) {


}