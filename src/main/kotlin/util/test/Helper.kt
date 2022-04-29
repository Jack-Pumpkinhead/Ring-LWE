package util.test

import math.integer.ulong.primeOf
import util.divides

/**
 * Created by CowardlyLion at 2022/4/27 18:29
 */
suspend fun findPrime(order: UInt): UInt {
    for (i in 1u..10000u) {
        val prime = primeOf(i)
        val primeDec = prime.toUInt() - 1u
        if (order.divides(primeDec)) {
//            println("prime: $prime, primeDec: ${primeDec.primeFactorization()}, order: ${order.primeFactorization()}")
            return prime.toUInt()
        }
    }
    error("prime too large")
}