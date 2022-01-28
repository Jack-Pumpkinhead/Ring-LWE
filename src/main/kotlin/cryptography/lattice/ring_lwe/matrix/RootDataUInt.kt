package cryptography.lattice.ring_lwe.matrix

import math.abstract_structure.Ring
import math.abstract_structure.instance.RingUInt
import math.integer.FactorizationUInt
import math.integer.FactorizationUIntPrime
import math.integer.FactorizationUIntPrimePower
import math.integer.operation.powerM
import math.operation.product
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/26 17:25
 */
class RootDataUInt<A>(val ring: Ring<A>, val root: A, val order: FactorizationUInt) {

    fun toPrimePower(): RootDataUIntPrimePower<A> {
        require(order.factors.size == 1)
        return RootDataUIntPrimePower(ring, root, order.factors[0])
    }

    fun toPrime(): RootDataUIntPrime<A> {
        require(order.factors.size == 1)
        val factorization = order.factors[0]
        require(factorization.power == 1u)
        return RootDataUIntPrime(ring, root, FactorizationUIntPrime(factorization.prime))
    }

    fun eulerTotient(): UInt = RingUInt.product(order.factors.map { it.eulerTotient() })

    fun primeSubroot(primeIndex: UInt): RootDataUIntPrime<A>  = subroot(FactorizationUIntPrime(order.factors[primeIndex.toInt()].prime))

    fun maximalPrimePowerSubroot(primeIndex: UInt): RootDataUIntPrimePower<A> {
        if (order.factors.size == 1) return this.toPrimePower()
        val factor = order.factors[primeIndex.toInt()]
        return RootDataUIntPrimePower(ring, ring.powerM(root, order.value / factor.value), factor)
    }

    fun allMaximalPrimePowerSubroot(): List<RootDataUIntPrimePower<A>> = order.factors.map { factor -> RootDataUIntPrimePower(ring, ring.powerM(root, order.value / factor.value), factor) }

    fun subrootReducePowerByOneAt(decreaseIndex: UInt): RootDataUInt<A> = RootDataUInt(ring, ring.powerM(root, order.factors[decreaseIndex.toInt()].prime), order.decreasePowerByOneAt(decreaseIndex))

    /**
     * if decreaseOrder is maximal, return 1 with order 1
     */
    fun subroot(decreaseOrder: List<UInt>): RootDataUInt<A> {
        require(decreaseOrder.size == order.factors.size)
        val orderFactorization1 = mutableListOf<FactorizationUIntPrimePower>()
        var totalDividend = 1u
        for (i in decreaseOrder.indices) {
            val decrease = decreaseOrder[i]
            if (decrease == 0u) {
                orderFactorization1 += order.factors[i]
            } else {
                val factor = order.factors[i]
                require(decrease <= factor.power)
                val power1 = factor.power - decrease
                if (power1 > 0u) {
                    val dividend = factor.prime.powerM(decrease)
                    totalDividend *= dividend
                    val primePower1 = factor.value / dividend
                    orderFactorization1 += FactorizationUIntPrimePower(primePower1, factor.prime, power1)
                }
            }
        }
        return RootDataUInt(ring, ring.powerM(root, totalDividend), FactorizationUInt(order.value / totalDividend, orderFactorization1))
    }

    fun subroot(suborder: FactorizationUInt): RootDataUInt<A> {
        require(order.value.mod(suborder.value) == 0u)
        return RootDataUInt(ring, ring.powerM(root, order.value / suborder.value), suborder)
    }

    fun subroot(suborder: FactorizationUIntPrimePower): RootDataUIntPrimePower<A> {
        require(order.value.mod(suborder.value) == 0u)
        return RootDataUIntPrimePower(ring, ring.powerM(root, order.value / suborder.value), suborder)
    }

    fun subroot(suborder: FactorizationUIntPrime): RootDataUIntPrime<A> {
        require(order.value.mod(suborder.value) == 0u)
        return RootDataUIntPrime(ring, ring.powerM(root, order.value / suborder.value), suborder)
    }


    override fun toString(): String {
        return "root: $root, $order = $order"
    }
}