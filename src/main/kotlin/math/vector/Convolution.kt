package math.vector

import math.abstract_structure.Ring
import math.integer.operation.modMinusUnsafe
import math.martix.VectorLike

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