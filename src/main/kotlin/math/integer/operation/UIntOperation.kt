package math.integer.operation

/**
 * Created by CowardlyLion at 2022/1/4 15:34
 */

/**
 * cannot use ([x] + [y]).mod([modulus]) because sometimes it may overflow, 下同
 */
fun modPlus(x: UInt, y: UInt, modulus: UInt): UInt {
    return (x.mod(modulus).toULong() + y.mod(modulus).toULong()).mod(modulus)
}

fun modUnaryMinus(a: UInt, modulus: UInt): UInt {
    val a1 = a.mod(modulus)
    return if (a1 == 0u) 0u else modulus - a1
}

/**
 * cannot use ([x]-[y]).mod([modulus]) because rounding by 2^32 change residue modulo [modulus]
 * see {UProgressionUtil.differenceModulo} at kotlin stdlib
 */
fun modMinus(x: UInt, y: UInt, modulus: UInt): UInt {
    val x1 = x.mod(modulus)
    val y1 = y.mod(modulus)
    return if (x1 >= y1) x1 - y1 else modulus - (y1 - x1)    //avoid rounding even if x1-y1+modulus is correct
}

/**
 * calculation in ULong is correct
 */
fun modTimes(x: UInt, y: UInt, modulus: UInt): UInt {
    return (x.mod(modulus).toULong() * y.mod(modulus).toULong()).mod(modulus)
}

/**
 * assume [x], [y] in [0, modulus)
 */
fun modPlusUnsafe(x: UInt, y: UInt, modulus: UInt): UInt = (x + y).mod(modulus)
fun modUnaryMinusUnsafe(a: UInt, modulus: UInt): UInt = if (a == 0u) 0u else modulus - a
fun modMinusUnsafe(x: UInt, y: UInt, modulus: UInt): UInt = if (x >= y) x - y else modulus - (y - x)
fun modTimesUnsafe(x: UInt, y: UInt, modulus: UInt): UInt = (x.toULong() * y.toULong()).mod(modulus)


/**
 * Montgomery's ladder.
 * @return [x]^[power] mod [modulus], 0^0 = 1
 */
fun modPowerM(x: UInt, power: UInt, modulus: UInt): UInt {
    require(modulus > 0u)
    return when (power) {
        0u   -> 1u.mod(modulus)
        1u   -> x.mod(modulus)
        else -> {
            var a = x.mod(modulus)
            var b = modTimes(a, a, modulus)
            var i = power.takeHighestOneBit() shr 1
            while (i != 0u) {
                if (power and i == 0u) {
                    b = modTimes(a, b, modulus)
                    a = modTimes(a, a, modulus)
                } else {
                    a = modTimes(a, b, modulus)
                    b = modTimes(b, b, modulus)
                }
                i = i shr 1
            }
            a
        }
    }
}

/**
 * Montgomery's ladder.
 * @return [this]^[power] mod 2^32, 0^0 = 1
 */
fun UInt.powerM(power: UInt): UInt = when (power) {
    0u   -> 1u
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
 * square version of fast power
 * @return [x]^[power] mod [modulus]
 */
fun modPowerS(x: UInt, power: UInt, modulus: UInt): UInt {
    require(modulus > 0u)
    var x2 = x
    var y = 1u
    var pow = power
    while (pow != 0u) {
        if (pow.and(1u) == 1u) {
            y = modTimes(y, x2, modulus)
        }
        x2 = modTimes(x2, x2, modulus)
        pow = pow shr 1
    }
    return y
}

/**
 * square version of fast power
 * @return [this]^[power]
 */
fun UInt.powerS(power: UInt): UInt {
    var x2 = this
    var y = 1u
    var pow = power
    while (pow != 0u) {
        if (pow.and(1u) == 1u) {
            y *= x2
        }
        x2 *= x2
        pow = pow shr 1
    }
    return y
}

fun powerOfTwoUInt(power: Int): UInt {
    require(power in 0..31) { "wrong power of $power" }
    return 1u.shl(power)
}
