package math.operations

/**
 * Created by CowardlyLion at 2022/1/4 15:34
 */

fun modPlus(x: UInt, y: UInt, modulus: UInt) = (x + y) % modulus
fun modMinus(x: UInt, y: UInt, modulus: UInt) = (x - y) % modulus
fun modTimes(x: UInt, y: UInt, modulus: UInt) = (x * y) % modulus

/**
 * Montgomery's ladder.
 * @param x assume x < modulus
 * @return x^power mod modulus, 0^0 = 1
 * */
fun modPowerM(x: UInt, power: UInt, modulus: UInt): UInt {
    require(x in 0u until modulus)
    return when (power) {
        0u   -> 1u % modulus
        1u   -> x
        else -> {
            var a = x
            var b = modTimes(x, x, modulus)
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
 * @return x^power mod (UInt.MAX_VALUE+1), 0^0 = 1
 * */
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
 * @param x assume x < modulus
 * */
fun modPowerSq(x: UInt, power: UInt, modulus: UInt): UInt {
    require(x in 0u until modulus)
    var x2 = x
    var y = 1u
    var pow = power
    while (pow != 0u) {
        if (pow % 2u == 1u) {
            y = modTimes(y, x2, modulus)
        }
        x2 = modTimes(x2, x2, modulus)
        pow = pow shr 1
    }
    return y
}

/**
 * square version of fast power
 * */
fun UInt.powerSq(power: UInt): UInt {
    var x2 = this
    var y = 1u
    var pow = power
    while (pow != 0u) {
        if (pow % 2u == 1u) {
            y *= x2
        }
        x2 *= x2
        pow = pow shr 1
    }
    return y
}

fun powerOfTwo(power: Int): UInt {
    require(power in 0..31) { "wrong power of $power" }
    return 1u.shl(power)
}
