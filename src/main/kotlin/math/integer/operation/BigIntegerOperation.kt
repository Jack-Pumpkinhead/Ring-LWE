package math.integer.operation

import com.ionspin.kotlin.bignum.integer.BigInteger

/**
 * Created by CowardlyLion at 2022/1/4 23:23
 */

val BigInteger.maxIndexOfOne: Long
    get() {
        return if (this == BigInteger.ZERO) -1
        else {
            var i = numberOfWords.toLong() * 63L
            while (!bitAt(i)) {
                i--
            }
            i
        }
    }

/**
 * Montgomery's ladder.
 * @return [x]^[power] mod [modulus], 0^0 = 1
 * */
fun modPowerM(x: BigInteger, power: BigInteger, modulus: BigInteger): BigInteger {
    require(power >= BigInteger.ZERO)
    require(modulus >= BigInteger.ONE)
    return when (power) {
        BigInteger.ZERO -> BigInteger.ONE.mod(modulus)
        BigInteger.ONE  -> x.mod(modulus)
        else            -> {
            var a = x.mod(modulus)
            var b = (a * a).mod(modulus)
            var index = power.maxIndexOfOne - 1
            while (index >= 0) {
                if (power.bitAt(index)) {
                    a = (a * b).mod(modulus)
                    b = (b * b).mod(modulus)
                } else {
                    b = (a * b).mod(modulus)
                    a = (a * a).mod(modulus)
                }
                index--
            }
            a
        }
    }
}


/**
 * Montgomery's ladder.
 * @return [this]^[power], 0^0 = 1
 * */
fun BigInteger.powerM(power: BigInteger): BigInteger {
    require(power >= BigInteger.ZERO)
    return when (power) {
        BigInteger.ZERO -> BigInteger.ONE
        BigInteger.ONE  -> this
        else            -> {
            var a = this
            var b = a * a
            var index = power.maxIndexOfOne - 1
            while (index >= 0) {
                if (power.bitAt(index)) {
                    a *= b
                    b *= b
                } else {
                    b *= a
                    a *= a
                }
                index--
            }
            a
        }
    }
}
