package math.vector

import math.abstract_structure.Ring
import math.integer.PrimePowerULong
import math.integer.operation.modMinusUnsafe

/**
 * Created by CowardlyLion at 2022/1/19 13:34
 */

fun <A> Ring<A>.convolution(a: VectorLike<A>, b: VectorLike<A>): List<A> {
    require(a.vectorSize > 0u)
    require(a.vectorSize == b.vectorSize)
    return if (a.vectorSize == 1u) listOf(multiply(a.vectorElementAtUnsafe(0u), b.vectorElementAtUnsafe(0u)))
    else List(a.vectorSize.toInt()) { i ->
        var sum = zero
        for (j in 0u until a.vectorSize) {
            sum = add(sum, multiply(a.vectorElementAtUnsafe(modMinusUnsafe(i.toUInt(), j, a.vectorSize)), b.vectorElementAtUnsafe(j)))
        }
        sum
    }
}

fun <A> Ring<A>.convolution(a: List<A>, b: List<A>): List<A> {
    require(a.isNotEmpty())
    require(a.size == b.size)
    return if (a.size == 1) listOf(multiply(a[0], b[0]))
    else List(a.size) { i ->
        var sum = zero
        for (j in 0u until a.size.toUInt()) {
            sum = add(sum, multiply(a[modMinusUnsafe(i.toUInt(), j, a.size.toUInt()).toInt()], b[j.toInt()]))
        }
        sum
    }
}

//TODO implement a fast convolution algorithm based on padding-repeating and radix-two FFT

//need a primitive 2^k-th root of unity, may not exist when field is ℤ/p
/*fun <A> Ring<A>.fastConvolution(a: List<A>, b: List<A>): List<A> {
    require(a.isNotEmpty())
    require(a.size == b.size)
    if (a.size == 1) return listOf(multiply(a[0], b[0]))


}*/

/**
 * return least 2^k satisfying 2^k >= 2[n]-1
 */
fun nextTwoPowerForDFT(n: UInt): PrimePowerULong {
    require(n > 1u)
    var power = 1u
    var twoPower = 2uL
    while (twoPower < n) {
        power++
        twoPower *= 2uL
    }
    return if (twoPower == n.toULong()) PrimePowerULong(2uL, power, twoPower)
    else {
        val bound = 2uL * n.toULong() - 1uL
        while (twoPower < bound) {
            power++
            twoPower *= 2uL
        }
        PrimePowerULong(2uL, power, twoPower)
    }
}
