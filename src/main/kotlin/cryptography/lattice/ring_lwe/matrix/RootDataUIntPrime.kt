package cryptography.lattice.ring_lwe.matrix

import kotlinx.coroutines.runBlocking
import math.abstract_structure.Ring
import math.integer.isPrime
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/26 17:28
 *
 * [order] need to be prime
 */
class RootDataUIntPrime<A>(val ring: Ring<A>, val root: A, val order: UInt) {

    init {
        lazyAssert2 {
            runBlocking {
                order.isPrime()
            }
        }
    }

}