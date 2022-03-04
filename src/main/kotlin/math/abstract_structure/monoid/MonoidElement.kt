package math.abstract_structure.monoid

import math.abstract_structure.Monoid

/**
 * Created by CowardlyLion at 2022/3/3 14:29
 *
 * [order] is least positive integer n s.t. [value]^n = 1, or 0 if n not exists.
 */
interface MonoidElement<A> {

    val monoid: Monoid<A>

    val value: A

    val order: UInt

    fun power(exponent: UInt): MonoidElement<A>

}