package math.abstract_structure.monoid

import math.abstract_structure.Monoid
import math.abstract_structure.algorithm.powerM
import math.integer.uint.gcd
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/3/3 14:38
 */
class MonoidElementCachePowerImpl<A>(override val monoid: Monoid<A>, override val value: A, override val order: UInt) : MonoidElementCachePower<A> {

    override fun power(exponent: UInt) =
        if (cacheComputed) {
            MonoidElementCachePowerSubPower(this, exponent)
        } else {
            MonoidElementCachePowerImpl(monoid, monoid.powerM(value, exponent), order / gcd(order, exponent))
        }


    override var cacheComputed: Boolean = false

    private val powers: List<A> by lazy {
        cacheComputed = true
        if (order == 1u) listOf(monoid.one)
        else {
            val list = mutableListOf(monoid.one, value)
            var x = value
            for (i in 2u until order) {
                x = monoid.multiply(x, value)
                list += x
            }
            lazyAssert2 {
                if (monoid.isExactComputation) {
                    assert(monoid.multiply(x, value) == monoid.one)
                }
            }
            list
        }
    }

    override fun cachedPower(exponent: UInt): A = powers[exponent.toInt()]

    override val inverse: MonoidElementCachePower<A> by lazy {
        MonoidElementCachePowerInverse(this)
    }



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MonoidElement<*>) return false
        if (monoid != other.monoid) return false
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int {
        var result = monoid.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "MonoidElementCachePowerImpl(monoid=$monoid, order=$order, value=$value)"
    }

}