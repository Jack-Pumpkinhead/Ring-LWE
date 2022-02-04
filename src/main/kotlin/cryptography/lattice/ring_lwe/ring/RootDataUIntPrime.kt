package cryptography.lattice.ring_lwe.ring

import math.abstract_structure.Ring
import math.abstract_structure.instance.*
import math.integer.FactorizationUIntPrime
import math.integer.operation.modUnaryMinus
import math.powerM
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/26 17:28
 *
 * [order] need to be prime
 */
class RootDataUIntPrime<A>(val ring: Ring<A>, val root: A, val order: FactorizationUIntPrime) {

    init {
        lazyAssert2 {
            require(order.value != 0u)
            if (order.value == 1u) {
                require(root == ring.one)
            }
        }
    }

    fun conjugate() = RootDataUIntPrime(ring, ring.powerM(root, order.value - 1u), order)

    private val powers: List<A> by lazy {
        if (order.value == 1u) listOf(ring.one)
        else {
            val list = mutableListOf(ring.one, root)
            var x = root
            for (i in 2u until order.value) {
                x = ring.multiply(x, root)
                list += x
            }
            lazyAssert2 {
                if (ring is RingModularUInt || ring is RingModularULong || ring is RingModularBigInteger || ring is FieldModularUInt || ring is FieldModularULong) {
                    assert(ring.multiply(x, root) == ring.one)
                }
            }
            list
        }
    }

    fun power(exponent: UInt) = powers[exponent.toInt()]

    fun inversePower(exponent: UInt) = powers[modUnaryMinus(exponent, order.value).toInt()]

}