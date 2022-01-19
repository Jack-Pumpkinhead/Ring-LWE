package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import math.abstract_structure.Ring
import math.integer.PrimePowerULong
import math.integer.operation.powerM
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/19 14:15
 */
class RootData<A>(val ring: Ring<A>, val root: A, val order: ULong, val orderFactorization: List<PrimePowerULong>) {

    fun subRootDataPrimePower(primeIndex: UInt): RootData<A> {
        val factor = orderFactorization[primeIndex.toInt()]
        val subRoot = ring.powerM(root, order / factor.primePower)
        return RootData(ring, subRoot, factor.primePower, listOf(factor))
    }

    fun subRootDataPrime(primeIndex: UInt): RootData<A> {
        val factor = orderFactorization[primeIndex.toInt()]
        var power = 1uL
        for (i in orderFactorization.indices) {
            if (i.toUInt() == primeIndex) continue
            power *= orderFactorization[i].primePower
        }
        if (factor.power > 1u) {
            power *= factor.primePower / factor.prime
        }

        return RootData(ring, ring.powerM(root, power), factor.prime, listOf(PrimePowerULong(factor.prime, 1u, factor.prime)))
    }

    fun allSubRootDataPrimePower(): List<RootData<A>> {
        return orderFactorization.indices.map { i -> subRootDataPrimePower(i.toUInt()) }
    }

    fun subRootData(decreaseIndex: UInt): RootData<A> {
        val factor = orderFactorization[decreaseIndex.toInt()]

        val orderFactorization1: List<PrimePowerULong>
        if (factor.power == 1u) {
            orderFactorization1 = orderFactorization.drop(1)
        } else {
            orderFactorization1 = orderFactorization.toMutableList()
            orderFactorization1[decreaseIndex.toInt()] = PrimePowerULong(factor.prime, factor.power - 1u, factor.primePower / factor.prime)
        }
        return RootData(ring, ring.powerM(root, factor.prime), order / factor.prime, orderFactorization1)
    }

    /**
     * if decreaseOrder is maximal, return 1 with order 1
     */
    fun subRootData(decreaseOrder: List<UInt>): RootData<A> {
        require(decreaseOrder.size == orderFactorization.size)
        val orderFactorization1 = mutableListOf<PrimePowerULong>()
        var totalDividend = 1uL
        for (i in decreaseOrder.indices) {
            val decrease = decreaseOrder[i]
            if (decrease == 0u) {
                orderFactorization1 += orderFactorization[i]
            } else {
                val factor = orderFactorization[i]
                require(decrease <= factor.power)
                val power1 = factor.power - decrease
                if (power1 > 0u) {
                    val dividend = factor.prime.powerM(decrease)
                    totalDividend *= dividend
                    val primePower1 = factor.primePower / dividend
                    orderFactorization1 += PrimePowerULong(factor.prime, power1, primePower1)
                }
            }
        }
        return RootData(ring, ring.powerM(root, totalDividend), order / totalDividend, orderFactorization1)
    }

    override fun toString(): String {
        return "root: $root, $order = $orderFactorization"
    }
}