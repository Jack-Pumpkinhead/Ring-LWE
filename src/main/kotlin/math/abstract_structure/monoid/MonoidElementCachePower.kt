package math.abstract_structure.monoid

import math.integer.uint.modUnaryMinus

/**
 * Created by CowardlyLion at 2022/3/3 14:31
 */
interface MonoidElementCachePower<A> : MonoidElementFiniteOrder<A> {

    override fun power(exponent: UInt): MonoidElementCachePower<A>

    val cacheComputed: Boolean

    fun cachedPower(exponent: UInt): A

    fun cachedInversePower(exponent: UInt): A = cachedPower(modUnaryMinus(exponent, order))

    override val inverse: MonoidElementCachePower<A>

}