package math.random

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import math.isEven
import math.isOdd
import util.ceilToBigInteger
import util.ceilToInteger
import util.toBigDecimal
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/2/8 20:47
 */

/**
 * von Neumann's algorithm generating a positive random number x with distribution e^(-x)
 */
fun Random.nextBinaryRandomNumberExponentialDistribution(): BinaryRandomNumber {
    var l = BigInteger.ZERO

    var x = BinaryRandomNumber(this, integer = BigInteger.ZERO)
    var n = lengthOfDescendingChain(x)

    while (n.isOdd()) {
        l++
        x = BinaryRandomNumber(this, integer = BigInteger.ZERO)
        n = lengthOfDescendingChain(x)
    }
    return BinaryRandomNumber(this, integer = l, fraction = x.fraction)  //inherit all existing fraction bits and keep async with x
}

/**
 * return maximum n s.t. x > u1 > ... > u_n
 */
fun Random.lengthOfDescendingChain(x: BinaryRandomNumber): BigInteger {
    var n = BigInteger.ZERO
    var uMin = x
    var u = BinaryRandomNumber(this, integer = BigInteger.ZERO)
    while (u < uMin) {
        n++
        uMin = u
        u = BinaryRandomNumber(this, integer = BigInteger.ZERO)
    }
    return n
}

/**
 * return true with probability e^(-1/2)
 */
fun Random.nextBooleanSpecialBernoulli() = lengthOfDescendingChainLessThanHalf().isEven()

/**
 * return maximum n s.t. 1/2 > u1 > ... > u_n
 */
fun Random.lengthOfDescendingChainLessThanHalf(): BigInteger {
    var u = BinaryRandomNumber(this, integer = BigInteger.ZERO)
    return if (u.lessThanHalf()) {
        var n = BigInteger.ONE
        var uMin = u
        u = BinaryRandomNumber(this, integer = BigInteger.ZERO)

        while (u < uMin) {
            n++
            uMin = u
            u = BinaryRandomNumber(this, integer = BigInteger.ZERO)
        }
        n
    } else BigInteger.ZERO
}

fun Random.nextBinaryRandomNumberStandardNormalDistribution(): BinaryRandomNumber {
    rerun@ while (true) {
        val k = nextNonNegativeIntegerSpecial()
        val k1 = (k + 1).shl(1)

        val x = BinaryRandomNumber(this, integer = BigInteger.ZERO)
        var trials = k + BigInteger.ONE
        while (trials.isPositive) {
            if (!nextBooleanSpecialBernoulli(x, k1)) {
                continue@rerun
            }
            trials--
        }

        return BinaryRandomNumber(this, this.nextBoolean(), k, x.fraction)  //inherit x's fractional part
    }
}

fun Random.nextDoubleNormalDistribution(mean: Double, sigma: Double): Double {
    val x = nextBinaryRandomNumberStandardNormalDistribution()
    return (x.toDouble() * sigma) + mean
}

/**
 * return n>=0 with relative probability density e^(-(n^2)/2)
 */
fun Random.nextNonNegativeIntegerSpecial(): BigInteger {
    rerun@ while (true) {
        val n = lengthOfTrueSpecialBernoulli()
        var trials = n * (n - 1)
        while (trials.isPositive) {
            if (!nextBooleanSpecialBernoulli()) {
                continue@rerun
            }
            trials--
        }
        return n
    }
}

fun Random.lengthOfTrueSpecialBernoulli(): BigInteger {
    var n = BigInteger.ZERO
    while (nextBooleanSpecialBernoulli()) {
        n++
    }
    return n
}

/**
 * return true with probability e^(-x(2k+x)/(2k+2))
 *
 * require [x] in (0, 1), k >= 0, [k1] = 2k + 2
 */
fun Random.nextBooleanSpecialBernoulli(x: BinaryRandomNumber, k1: BigInteger): Boolean {
    var n = BigInteger.ZERO
    var uMin = x
    while (true) {
        val u = BinaryRandomNumber(this, integer = BigInteger.ZERO)
        if (u < uMin) {
            val f = threeWaySelectorSpecial(k1)
            if (f > 0 || (f == 0 && BinaryRandomNumber(this) < x)) {
                uMin = u
                n++
                continue
            }
        }
        break
    }
    return n.isEven()
}

/**
 * require [m] >= 2
 *
 * return (-1, 0, 1) with probabilities (1/m, 1/m, 1 - 2/m)
 */
fun Random.threeWaySelectorSpecial(m: BigInteger): Int {
    var n1 = BigInteger.ONE
    var n2 = BigInteger.TWO
    val bits = RandomBit(this)

    while (true) {
        if (bits.nextBit()) {
            n1 = n1.shl(1) - m
            n2 = n2.shl(1) - m
        } else {
            n1 = n1.shl(1)
            n2 = n2.shl(1)
        }
        //always have n1 < n2

        when {
            !n2.isPositive              -> return 1     //occur most frequently (x > 1 - 2/m) when m >= 4
            n1 >= m                     -> return -1
            (!n1.isPositive) && n2 >= m -> return 0 // 1/m < x < 2/m
        }
    }
}

fun Random.nextIntegerNormalDistribution(mu: BigDecimal, sigma: BigDecimal, sigmaInv: BigDecimal): BigInteger {
    val randomBit = RandomBit(this)
    val ceilSigma = sigma.ceilToBigInteger()

    rerun@ while (true) {
        val k = nextNonNegativeIntegerSpecial()

        val sign = randomBit.nextBit()

        val t = if (sign) {
            sigma * k.toBigDecimal() + mu
        } else {
            sigma * k.toBigDecimal() - mu
        }
        val ceilT = t.ceilToInteger()
        val j = this.nextBigIntegerFDR(ceilSigma)
        val x = (ceilT - t + j.toBigDecimal()) * sigmaInv

        if (x < BigDecimal.ONE) {
            if (k.isPositive || x.isPositive || sign) {
                val k1 = (k + 1).shl(1)

                var trials = k + BigInteger.ONE
                while (trials.isPositive) {
                    if (!nextBooleanSpecialBernoulli(x, k1)) {
                        continue@rerun
                    }
                    trials--
                }

                return if (sign) {
                    ceilT.toBigInteger() + j
                } else {
                    -(ceilT.toBigInteger() + j)
                }
            }
        }
    }
}

/**
 * return true with probability e^(-x(2k+x)/(2k+2))
 *
 * require [x] in [0, 1), k >= 0, [k1] = 2k + 2
 */
fun Random.nextBooleanSpecialBernoulli(x: BigDecimal, k1: BigInteger): Boolean {
    var n = BigInteger.ZERO

    val u = BinaryRandomNumber(this, integer = BigInteger.ZERO)
    if (u.absoluteValueCompareToNonNegative(x) < 0) {
        val f = threeWaySelectorSpecial(k1)
        if (f > 0 || (f == 0 && BinaryRandomNumber(this).absoluteValueCompareToNonNegative(x) < 0)) {
            var uMin = u
            n++

            while (true) {
                val u1 = BinaryRandomNumber(this, integer = BigInteger.ZERO)
                if (u1 < uMin) {
                    val f1 = threeWaySelectorSpecial(k1)
                    if (f1 > 0 || (f1 == 0 && BinaryRandomNumber(this).absoluteValueCompareToNonNegative(x) < 0)) {
                        uMin = u1
                        n++
                        continue
                    }
                }
                break
            }
        }
    }

    return n.isEven()
}
