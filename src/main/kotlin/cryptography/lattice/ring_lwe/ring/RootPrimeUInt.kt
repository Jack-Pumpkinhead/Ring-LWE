package cryptography.lattice.ring_lwe.ring

import math.abstract_structure.Ring
import math.abstract_structure.algorithm.powerM
import math.integer.big_integer.modular.RingModularBigInteger
import math.integer.uint.factored.PrimeUInt
import math.integer.uint.modUnaryMinus
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.RingModularUInt
import math.integer.ulong.modular.FieldModularULong
import math.integer.ulong.modular.RingModularULong
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/26 17:28
 *
 * [order] need to be prime
 */
class RootPrimeUInt<A>(override val ring: Ring<A>, override val root: A, override val order: PrimeUInt) : AbstractRootPrimeUInt<A> {

    override val inverse: RootPrimeUInt<A> by lazy { RootPrimeUInt(ring, ring.powerM(root, order.value - 1u), order) }

    private val powers: List<A> by lazy {
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