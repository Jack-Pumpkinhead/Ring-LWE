package math.integer.ulong

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger

/**
 * Created by CowardlyLion at 2022/1/18 18:00
 */

fun modPlus(x: ULong, y: ULong, modulus: ULong): ULong {
    return (x.mod(modulus) + y.mod(modulus)).mod(modulus)
}

fun modUnaryMinus(a: ULong, modulus: ULong): ULong {
    val a1 = a.mod(modulus)
    return if (a1 == 0uL) 0uL else modulus - a1
}

fun modMinus(x: ULong, y: ULong, modulus: ULong): ULong {
    val x1 = x.mod(modulus)
    val y1 = y.mod(modulus)
    return if (x1 >= y1) x1 - y1 else modulus - (y1 - x1)
}

fun modTimes(x: ULong, y: ULong, modulus: ULong): ULong {
    return (x.mod(modulus).toBigInteger() * y.mod(modulus).toBigInteger()).mod(modulus.toBigInteger()).ulongValue()
}

/**
 * assume [x], [y] in [0, modulus)
 */
fun modPlusUnsafe(x: ULong, y: ULong, modulus: ULong): ULong = (x + y).mod(modulus)
fun modUnaryMinusUnsafe(a: ULong, modulus: ULong): ULong = if (a == 0uL) 0uL else modulus - a
fun modMinusUnsafe(x: ULong, y: ULong, modulus: ULong): ULong = if (x >= y) x - y else modulus - (y - x)
fun modTimesUnsafe(x: ULong, y: ULong, modulus: ULong): ULong = (x.toBigInteger() * y.toBigInteger()).mod(modulus.toBigInteger()).ulongValue()

fun ULong.powerM(power: UInt): ULong = when (power) {
    0u   -> 1uL
    1u   -> this
    else -> {
        var a = this
        var b = a * a
        var i = power.takeHighestOneBit() shr 1
        while (i != 0u) {
            if (power and i == 0u) {
                b *= a
                a *= a
            } else {
                a *= b
                b *= b
            }
            i = i shr 1
        }
        a
    }
}

/**
 * @return [x]^[power] mod [modulus]
 */
fun modPowerS(x: ULong, power: ULong, modulus: ULong): ULong {
    require(modulus > 0uL)
    var x1 = x.toBigInteger()
    var y = BigInteger.ONE
    val m1 = modulus.toBigInteger()
    var pow = power
    while (pow != 0uL) {
        if (pow.and(1uL) == 1uL) {
            y = (y * x1).mod(m1)
        }
        x1 = (x1 * x1).mod(m1)
        pow = pow shr 1
    }
    return y.ulongValue()
}