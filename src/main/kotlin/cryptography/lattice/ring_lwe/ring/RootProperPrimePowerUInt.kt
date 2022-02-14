package cryptography.lattice.ring_lwe.ring

import math.abstract_structure.Ring
import math.abstract_structure.algorithm.powerM
import math.integer.big_integer.modular.RingModularBigInteger
import math.integer.uint.*
import math.integer.uint.factored.PrimeUInt
import math.integer.uint.factored.ProperPrimePowerUInt
import math.integer.uint.factored.UIntP
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.RingModularUInt
import math.integer.ulong.modular.FieldModularULong
import math.integer.ulong.modular.RingModularULong
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/26 17:29
 */
class RootProperPrimePowerUInt<A>(override val ring: Ring<A>, override val root: A, override val order: ProperPrimePowerUInt) : RootPrimePowerUInt<A> {

    /**
     * reduce power by 1
     */
    fun subrootReducePowerOne(): RootPrimePowerUInt<A> =
        if (order.power == 2u) {
            RootPrimeUInt(ring, ring.powerM(root, order.prime), order.reducePowerByOneToPrimeUnsafe())
        } else {
            RootProperPrimePowerUInt(ring, ring.powerM(root, order.prime), order.reducePowerByOneUnsafe())
        }

    fun subrootReducePower(decreasePower: UInt): RootPrimePowerUInt<A> {
        if (decreasePower == 0u) return this
        require(decreasePower < order.power)
        val power1 = order.power - decreasePower

        val dividend = order.prime.powerM(decreasePower)

        return if (power1 == 1u) {
            RootPrimeUInt(ring, ring.powerM(root, dividend), UIntP(order.prime))
        } else {
            RootProperPrimePowerUInt(ring, ring.powerM(root, dividend), ProperPrimePowerUInt(order.value / dividend, order.prime, power1))
        }
    }

    fun primeSubroot(): RootPrimeUInt<A> = RootPrimeUInt(ring, ring.powerM(root, order.value / order.prime), PrimeUInt(order.prime))

    override val inverse: RootProperPrimePowerUInt<A> by lazy {
        RootProperPrimePowerUInt(ring, ring.powerM(root, order.value - 1u), order)
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

    override fun cachedPower(exponent: UInt) = powers[exponent.toInt()]

    override fun cachedInversePower(exponent: UInt) = powers[modUnaryMinus(exponent, order.value).toInt()]


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RootUIntPPPI<*>) return false

        if (ring != other.ring) return false
        if (root != other.root) return false
        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ring.hashCode()
        result = 31 * result + (root?.hashCode() ?: 0)
        result = 31 * result + order.hashCode()
        return result
    }

}