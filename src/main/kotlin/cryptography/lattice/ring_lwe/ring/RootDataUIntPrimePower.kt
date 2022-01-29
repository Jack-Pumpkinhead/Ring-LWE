package cryptography.lattice.ring_lwe.ring

import math.abstract_structure.Ring
import math.abstract_structure.instance.*
import math.integer.FactorizationUIntPrime
import math.integer.FactorizationUIntPrimePower
import math.integer.operation.modUnaryMinus
import math.integer.operation.powerM
import math.powerM
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/26 17:29
 */
class RootDataUIntPrimePower<A>(val ring: Ring<A>, val root: A, val order: FactorizationUIntPrimePower) {

    init {
        lazyAssert2 {
            require(order.value != 0u)
            if (order.value == 1u) {
                require(root == ring.one)
            }
        }
    }

    fun toPrime(): RootDataUIntPrime<A> {
        require(order.power == 1u)
        return RootDataUIntPrime(ring, root, FactorizationUIntPrime(order.prime))
    }

    /**
     * reduce power by 1
     */
    fun subRootData(): RootDataUIntPrimePower<A> {
        require(order.power > 1u)
        return RootDataUIntPrimePower(ring, ring.powerM(root, order.prime), order.reducePowerByOne())
    }

    fun subRootData(decreasePower: UInt): RootDataUIntPrimePower<A> {
        if (decreasePower == 0u) return this
        require(decreasePower <= order.power)
        val power1 = order.power - decreasePower
        require(power1 > 0u)

        val dividend = order.prime.powerM(decreasePower)
        return RootDataUIntPrimePower(ring, ring.powerM(root, dividend), FactorizationUIntPrimePower(order.value / dividend, order.prime, power1))
    }

    fun primeSubroot(): RootDataUIntPrime<A> {
        if (order.power == 1u) return this.toPrime()
        return RootDataUIntPrime(ring, ring.powerM(root, order.value/order.prime), FactorizationUIntPrime(order.prime))
    }

    /**
     * previous method should not reference to here
     */
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