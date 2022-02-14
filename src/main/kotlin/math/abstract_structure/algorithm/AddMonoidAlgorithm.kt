package math.abstract_structure.algorithm

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.abstract_structure.AddMonoid
import math.integer.big_integer.maxIndexOfOne

/**
 * Created by CowardlyLion at 2022/2/5 13:23
 *
 *     Monoid<A> -> AddMonoid<A>
 *     one -> zero
 *     multiply -> add
 *     power -> multiple
 */

/**
 * Montgomery's ladder for computing multiple of [x] in a monoid A.
 * */
fun <A> AddMonoid<A>.multipleM(x: A, multiple: UInt): A = when (multiple) {
    0u   -> zero
    1u   -> x
    else -> {
        var a = x
        var b = add(x, x)
        var i = multiple.takeHighestOneBit() shr 1
        while (i != 0u) {
            if (multiple and i == 0u) {
                b = add(a, b)
                a = add(a, a)
            } else {
                a = add(a, b)
                b = add(b, b)
            }
            i = i shr 1
        }
        a
    }
}

fun <A> AddMonoid<A>.multipleM(x: A, multiple: ULong): A = when (multiple) {
    0uL  -> zero
    1uL  -> x
    else -> {
        var a = x
        var b = add(x, x)
        var i = multiple.takeHighestOneBit() shr 1
        while (i != 0uL) {
            if (multiple and i == 0uL) {
                b = add(a, b)
                a = add(a, a)
            } else {
                a = add(a, b)
                b = add(b, b)
            }
            i = i shr 1
        }
        a
    }
}


/**
 * Montgomery's ladder for computing multiple of [x] in a monoid [A].
 * */
fun <A> AddMonoid<A>.multipleM(x: A, multiple: BigInteger): A {
    require(multiple >= BigInteger.ZERO)
    return when (multiple) {
        BigInteger.ZERO -> zero
        BigInteger.ONE  -> x
        else            -> {
            var a = x
            var b = add(x, x)
            var index = multiple.maxIndexOfOne - 1
            while (index >= 0) {
                if (multiple.bitAt(index)) {
                    a = add(a, b)
                    b = add(b, b)
                } else {
                    b = add(a, b)
                    a = add(a, a)
                }
                index--
            }
            a
        }
    }
}

/**
 * square version of fast multiple,
 * @return [x]^[multiple]
 * */
fun <A> AddMonoid<A>.multipleS(x: A, multiple: UInt): A {
    var xPow = x
    var y = zero
    var pow = multiple
    while (pow != 0u) {
        if (pow % 2u == 1u) {
            y = add(y, xPow)
        }
        xPow = add(xPow, xPow)
        pow = pow shr 1
    }
    return y
}

fun <A> AddMonoid<A>.multipleS(x: A, multiple: ULong): A {
    var xPow = x
    var y = zero
    var pow = multiple
    while (pow != 0uL) {
        if (pow % 2uL == 1uL) {
            y = add(y, xPow)
        }
        xPow = add(xPow, xPow)
        pow = pow shr 1
    }
    return y
}

/**
 * square version of fast multiple
 * @return [x]^[multiple]
 * */
fun <A> AddMonoid<A>.multipleS(x: A, multiple: BigInteger): A {
    var xPow = x
    var y = zero
    var pow = multiple
    while (pow != BigInteger.ZERO) {
        if (pow % BigInteger.TWO == BigInteger.ONE) {
            y = add(y, xPow)
        }
        xPow = add(xPow, xPow)
        pow = pow shr 1
    }
    return y
}

fun <A> AddMonoid<A>.multiple(x: A, multiple: UInt): A {
    if (multiple == 0u) return this.zero
    if (multiple == 1u) return x
    var xPow = x
    for (i in 1u until multiple) {
        xPow = add(xPow, x)
    }
    return xPow
}

fun <A> AddMonoid<A>.multiple(x: A, multiple: ULong): A {
    if (multiple == 0uL) return this.zero
    if (multiple == 1uL) return x
    var xPow = x
    for (i in 1uL until multiple) {
        xPow = add(xPow, x)
    }
    return xPow
}
