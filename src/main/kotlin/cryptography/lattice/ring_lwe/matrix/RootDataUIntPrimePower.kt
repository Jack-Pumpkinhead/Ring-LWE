package cryptography.lattice.ring_lwe.matrix

import math.abstract_structure.Ring
import math.integer.PrimePowerUInt
import math.integer.operation.powerM
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/26 17:29
 */
class RootDataUIntPrimePower<A>(val ring: Ring<A>, val root: A, val order: PrimePowerUInt) {

    fun toPrime(): RootDataUIntPrime<A> {
        require(order.power == 1u)
        return RootDataUIntPrime(ring, root, order.prime)
    }

    fun eulerTotient(): UInt = order.eulerTotient()

    /**
     * reduce power by 1
     */
    fun subRootData(): RootDataUIntPrimePower<A> {
        require(order.power > 1u)
        return RootDataUIntPrimePower(ring, ring.powerM(root, order.prime), order.reducePower())
    }

    fun subRootData(decreasePower: UInt): RootDataUIntPrimePower<A> {
        if (decreasePower == 0u) return this
        require(decreasePower <= order.power)
        val power1 = order.power - decreasePower
        require(power1 > 0u)

        val dividend = order.prime.powerM(decreasePower)
        return RootDataUIntPrimePower(ring, ring.powerM(root, dividend), PrimePowerUInt(order.prime, power1, order.primePower / dividend))
    }

    fun primeSubroot(): RootDataUIntPrime<A> {
        if (order.power == 1u) return this.toPrime()

        val dividend = order.prime.powerM(order.power - 1u)
        return RootDataUIntPrime(ring, ring.powerM(root, dividend), order.prime)
    }


}