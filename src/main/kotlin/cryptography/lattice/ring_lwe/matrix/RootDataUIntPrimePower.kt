package cryptography.lattice.ring_lwe.matrix

import math.abstract_structure.Ring
import math.integer.FactorizationUIntPrime
import math.integer.FactorizationUIntPrimePower
import math.integer.operation.powerM
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/26 17:29
 */
class RootDataUIntPrimePower<A>(val ring: Ring<A>, val root: A, val order: FactorizationUIntPrimePower) {

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


}