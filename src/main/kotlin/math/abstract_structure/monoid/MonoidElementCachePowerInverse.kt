package math.abstract_structure.monoid

import math.abstract_structure.Monoid
import math.integer.uint.modUnaryMinus

/**
 * Created by CowardlyLion at 2022/3/3 15:03
 */
class MonoidElementCachePowerInverse<A>(override val inverse: MonoidElementCachePower<A>) : MonoidElementCachePower<A> {

    override val monoid: Monoid<A> get() = inverse.monoid
    override val value: A = monoid.inverse(inverse.value)
    override val order: UInt get() = inverse.order

    override fun power(exponent: UInt) = inverse.power(modUnaryMinus(exponent, order))
    override val cacheComputed: Boolean get() = inverse.cacheComputed
    override fun cachedPower(exponent: UInt) = inverse.cachedInversePower(exponent)
    override fun cachedInversePower(exponent: UInt) = inverse.cachedPower(exponent)

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
        return "MonoidElementCachePowerInverse(monoid=$monoid, order=$order, value=$value)"
    }


}