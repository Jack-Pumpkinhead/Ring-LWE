package math.random

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.isOdd
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/2/8 20:47
 */

/**
 * von Neumann's algorithm generating a positive random number x with distribution e^(-x)
 */
fun Random.nextBinaryRandomNumberExponentialDistribution(): BinaryRandomNumber {
    var l = BigInteger.ZERO

    var x = BinaryRandomNumber(this, BigInteger.ZERO)
    var n = lengthOfDescendingChain(x)

    while (n.isOdd()) {
        l++
        x = BinaryRandomNumber(this, BigInteger.ZERO)
        n = lengthOfDescendingChain(x)
    }
    return BinaryRandomNumber(this, l, x.fraction)  //inherit all existing fraction bits and keep async with x
}

fun Random.lengthOfDescendingChain(x: BinaryRandomNumber): BigInteger {
    var n = BigInteger.ZERO
    var uMin = x
    var u = BinaryRandomNumber(this, BigInteger.ZERO)
    while (u < uMin) {
        n++
        uMin = u
        u = BinaryRandomNumber(this, BigInteger.ZERO)
    }
    return n
}