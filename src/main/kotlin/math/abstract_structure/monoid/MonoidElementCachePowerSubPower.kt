package math.abstract_structure.monoid

import math.abstract_structure.Monoid
import math.abstract_structure.algorithm.powerM
import math.integer.uint.gcd
import math.integer.uint.modTimes
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/3/3 15:08
 *
 * power of parent
 */
class MonoidElementCachePowerSubPower<A>(val parent: MonoidElementCachePower<A>, val power: UInt, override val order: UInt = parent.order / gcd(parent.order, power)) : MonoidElementCachePower<A> {

    init {
        lazyAssert2 {
            if (parent.order > 1u) {
                assert(power < parent.order)
            }
        }
    }

    override val monoid: Monoid<A> get() = parent.monoid
    override val value: A = monoid.powerM(parent.value, power)

    override fun power(exponent: UInt) = parent.power(modTimes(exponent, power, parent.order))
    override val cacheComputed: Boolean get() = parent.cacheComputed
    override fun cachedPower(exponent: UInt) = parent.cachedPower(modTimes(exponent, power, parent.order))

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

}