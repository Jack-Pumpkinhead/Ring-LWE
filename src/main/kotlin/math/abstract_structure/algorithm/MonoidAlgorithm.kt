package math.abstract_structure.algorithm

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.abstract_structure.Monoid
import math.integer.operation.maxIndexOfOne

/**
 * Created by CowardlyLion at 2022/1/5 19:25
 */

/**
 * Montgomery's ladder for computing power of [x] in a monoid A.
 * */
fun <A> Monoid<A>.powerM(x: A, power: UInt): A = when (power) {
    0u   -> one
    1u   -> x
    else -> {
        var a = x
        var b = multiply(x, x)
        var i = power.takeHighestOneBit() shr 1
        while (i != 0u) {
            if (power and i == 0u) {
                b = multiply(a, b)
                a = multiply(a, a)
            } else {
                a = multiply(a, b)
                b = multiply(b, b)
            }
            i = i shr 1
        }
        a
    }
}

fun <A> Monoid<A>.powerM(x: A, power: ULong): A = when (power) {
    0uL  -> one
    1uL  -> x
    else -> {
        var a = x
        var b = multiply(x, x)
        var i = power.takeHighestOneBit() shr 1
        while (i != 0uL) {
            if (power and i == 0uL) {
                b = multiply(a, b)
                a = multiply(a, a)
            } else {
                a = multiply(a, b)
                b = multiply(b, b)
            }
            i = i shr 1
        }
        a
    }
}


/**
 * Montgomery's ladder for computing power of [x] in a monoid [A].
 * */
fun <A> Monoid<A>.powerM(x: A, power: BigInteger): A {
    require(power >= BigInteger.ZERO)
    return when (power) {
        BigInteger.ZERO -> one
        BigInteger.ONE  -> x
        else            -> {
            var a = x
            var b = multiply(x, x)
            var index = power.maxIndexOfOne - 1
            while (index >= 0) {
                if (power.bitAt(index)) {
                    a = multiply(a, b)
                    b = multiply(b, b)
                } else {
                    b = multiply(a, b)
                    a = multiply(a, a)
                }
                index--
            }
            a
        }
    }
}

/**
 * square version of fast power,
 * @return [x]^[power]
 * */
fun <A> Monoid<A>.powerS(x: A, power: UInt): A {
    var xPow = x
    var y = one
    var pow = power
    while (pow != 0u) {
        if (pow % 2u == 1u) {
            y = multiply(y, xPow)
        }
        xPow = multiply(xPow, xPow)
        pow = pow shr 1
    }
    return y
}

fun <A> Monoid<A>.powerS(x: A, power: ULong): A {
    var xPow = x
    var y = one
    var pow = power
    while (pow != 0uL) {
        if (pow % 2uL == 1uL) {
            y = multiply(y, xPow)
        }
        xPow = multiply(xPow, xPow)
        pow = pow shr 1
    }
    return y
}

/**
 * square version of fast power
 * @return [x]^[power]
 * */
fun <A> Monoid<A>.powerS(x: A, power: BigInteger): A {
    var xPow = x
    var y = one
    var pow = power
    while (pow != BigInteger.ZERO) {
        if (pow % BigInteger.TWO == BigInteger.ONE) {
            y = multiply(y, xPow)
        }
        xPow = multiply(xPow, xPow)
        pow = pow shr 1
    }
    return y
}

fun <A> Monoid<A>.power(x: A, power: UInt): A {
    if (power == 0u) return this.one
    if (power == 1u) return x
    var xPow = x
    for (i in 1u until power) {
        xPow = multiply(xPow, x)
    }
    return xPow
}

fun <A> Monoid<A>.power(x: A, power: ULong): A {
    if (power == 0uL) return this.one
    if (power == 1uL) return x
    var xPow = x
    for (i in 1uL until power) {
        xPow = multiply(xPow, x)
    }
    return xPow
}
