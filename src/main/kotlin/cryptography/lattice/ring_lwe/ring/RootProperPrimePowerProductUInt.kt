package cryptography.lattice.ring_lwe.ring

import math.abstract_structure.Ring
import math.abstract_structure.algorithm.powerM
import math.integer.big_integer.modular.RingModularBigInteger
import math.integer.uint.*
import math.integer.uint.factored.*
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.RingModularUInt
import math.integer.ulong.modular.FieldModularULong
import math.integer.ulong.modular.RingModularULong
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/26 17:25
 */
class RootProperPrimePowerProductUInt<A>(override val ring: Ring<A>, override val root: A, override val order: UIntPPP) : RootPrimePowerProductUInt<A> {

    /**
     * require suborder divide order
     */
    fun subrootUnsafe(suborder: UIntP) = RootUIntP(ring, ring.powerM(root, order.value / suborder.value), suborder)
    fun subrootUnsafe(suborder: UIntPP) = RootUIntPP(ring, ring.powerM(root, order.value / suborder.value), suborder)
    fun subrootUnsafe(suborder: UIntPPP) = RootUIntPPP(ring, ring.powerM(root, order.value / suborder.value), suborder)
    fun subrootUnsafe(suborder: UIntPPI): RootUIntPPI<A> =
        when (suborder) {
            is UIntPP -> subrootUnsafe(suborder)
            is UIntP  -> subrootUnsafe(suborder)
            else      -> error("unknown type of order $suborder, class: ${suborder::class}")
        }

    fun subrootUnsafe(suborder: UIntPPPI) =
        when (suborder) {
            is UIntPPP -> subrootUnsafe(suborder)
            is UIntPP  -> subrootUnsafe(suborder)
            is UIntP   -> subrootUnsafe(suborder)
            else       -> error("unknown type of order $suborder, class: ${suborder::class}")
        }

    fun subroot(suborder: UIntP): RootUIntP<A> {
        require(order.value.mod(suborder.value) == 0u)
        return subrootUnsafe(suborder)
    }

    fun subroot(suborder: UIntPP): RootUIntPP<A> {
        require(order.value.mod(suborder.value) == 0u)
        return subrootUnsafe(suborder)
    }

    fun subroot(suborder: UIntPPP): RootUIntPPP<A> {
        require(order.value.mod(suborder.value) == 0u)
        return subrootUnsafe(suborder)
    }

    fun subroot(suborder: UIntPPI): RootUIntPPI<A> {
        require(order.value.mod(suborder.value) == 0u)
        return subrootUnsafe(suborder)
    }

    fun subroot(suborder: UIntPPPI): RootUIntPPPI<A> {
        require(order.value.mod(suborder.value) == 0u)
        return subrootUnsafe(suborder)
    }


    /**
     * [i] is index of prime power in the factorization of order
     */
    fun primeSubrootAt(i: UInt): RootPrimeUInt<A> = subrootUnsafe(UIntP(order.factors[i.toInt()].prime))


    /**
     * [i] is index of prime power in the factorization of order
     */
    fun maximalPrimePowerSubrootAt(i: UInt): RootUIntPPI<A> = subrootUnsafe(order.factors[i.toInt()])

    fun allMaximalPrimePowerSubroot(): List<RootUIntPPI<A>> = order.factors.map { factor -> subrootUnsafe(factor) }

    fun subrootReducePowerByOneAt(decreaseIndex: UInt): RootUIntPPPI<A> =
        when (val newOrder = order.decreasePowerByOneAt(decreaseIndex)) {
            is UIntPPP -> RootUIntPPP(ring, ring.powerM(root, order.factors[decreaseIndex.toInt()].prime), newOrder)
            is UIntPP  -> RootUIntPP(ring, ring.powerM(root, order.factors[decreaseIndex.toInt()].prime), newOrder)
            is UIntP   -> RootUIntP(ring, ring.powerM(root, order.factors[decreaseIndex.toInt()].prime), newOrder)
            else       -> error("unknown type of order $newOrder, class: ${newOrder::class}")
        }

    override val inverse: RootProperPrimePowerProductUInt<A> by lazy { RootProperPrimePowerProductUInt(ring, ring.powerM(root, order.value - 1u), order) }

    override fun toString(): String {
        return "root: $root, $order = $order"
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