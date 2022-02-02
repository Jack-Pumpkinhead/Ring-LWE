package math.random

import math.abstract_structure.instance.RingModularUInt
import math.abstract_structure.instance.RingModularULong
import math.abstract_structure.instance.RingUInt
import math.integer.FactorizationUInt
import math.integer.FactorizationUIntPrimePower
import math.integer.ceilLog2
import math.integer.isPrime
import math.integer.modular.ModularUInt
import math.integer.modular.ModularULong
import math.martix.concrete.OrdinaryMatrix
import math.martix.concrete.OrdinarySquareMatrix
import math.martix.matrix
import math.martix.squareMatrix
import math.twoPower32
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.random.nextULong

/**
 * Created by CowardlyLion at 2022/1/12 18:18
 */

fun <T> Random.randomizedElements(weights: DoubleArray, vararg elements: T): T {
    require(weights.isNotEmpty())
    require(weights.size == elements.size)
    val value = nextDouble() * weights.sum()
    var sum = 0.0
    for (i in weights.indices) {
        if (weights[i] != 0.0) {
            sum += weights[i]
            if (sum >= value) {
                return elements[i]
            }
        }
    }
    return elements.last()
}

fun Random.randomUIntMatrix(rows: UInt, columns: UInt, bound: UIntRange) = RingUInt.matrix(rows, columns) { _, _ -> nextUInt(bound) }
fun Random.randomUIntMatrix(rows: UInt, columnsRange: UIntRange, bound: UIntRange) = RingUInt.matrix(rows, nextUInt(columnsRange)) { _, _ -> nextUInt(bound) }
fun Random.randomUIntMatrix(rowsRange: UIntRange, columns: UInt, bound: UIntRange) = RingUInt.matrix(nextUInt(rowsRange), columns) { _, _ -> nextUInt(bound) }
fun Random.randomUIntMatrix(rowsRange: UIntRange, columnsRange: UIntRange, bound: UIntRange) = RingUInt.matrix(nextUInt(rowsRange), nextUInt(columnsRange)) { _, _ -> nextUInt(bound) }

fun Random.randomSquareUIntMatrix(size: UInt, bound: UIntRange) = RingUInt.squareMatrix(size) { _, _ -> nextUInt(bound) }
fun Random.randomSquareUIntMatrix(sizeRange: UIntRange, bound: UIntRange) = RingUInt.squareMatrix(nextUInt(sizeRange)) { _, _ -> nextUInt(bound) }

fun Random.randomMultiplicableUIntMatrices(matrices: UInt, size: UIntRange, bound: UIntRange): List<OrdinaryMatrix<UInt>> {
    var a = nextUInt(size)
    return List(matrices.toInt()) {
        val b = nextUInt(size)
        val matrix = randomUIntMatrix(a, b, bound)
        a = b
        matrix
    }
}

fun Random.randomUIntMatrices(matrices: UInt, sizeRange: UIntRange, bound: UIntRange): List<OrdinaryMatrix<UInt>> {
    return List(matrices.toInt()) {
        randomUIntMatrix(nextUInt(sizeRange), nextUInt(sizeRange), bound)
    }
}

fun Random.randomSquareUIntMatrices(matrices: UInt, size: UInt, bound: UIntRange): List<OrdinaryMatrix<UInt>> {
    return List(matrices.toInt()) {
        randomSquareUIntMatrix(size, bound)
    }
}

fun Random.randomSquareUIntMatrices(matrices: UInt, sizeRange: UIntRange, bound: UIntRange): List<OrdinarySquareMatrix<UInt>> {
    val size = nextUInt(sizeRange)
    return List(matrices.toInt()) {
        randomSquareUIntMatrix(size, bound)
    }
}

fun RingModularULong.randomModularULongMatrix(rowsRange: UIntRange, columnsRange: UIntRange, randomness: Random = Random) = this.matrix(randomness.nextUInt(rowsRange), randomness.nextUInt(columnsRange)) { _, _ -> ModularULong(modulus, randomness.nextULong(modulus)) }
fun RingModularUInt.randomModularUIntMatrix(rows: UInt, columns: UInt, randomness: Random = Random) = this.matrix(rows, columns) { _, _ -> ModularUInt(modulus, randomness.nextUInt(modulus)) }
fun RingModularUInt.randomModularUIntMatrix(rows: UInt, columnsRange: UIntRange, randomness: Random = Random) = this.matrix(rows, randomness.nextUInt(columnsRange)) { _, _ -> ModularUInt(modulus, randomness.nextUInt(modulus)) }
fun RingModularUInt.randomModularUIntMatrix(rowsRange: UIntRange, columns: UInt, randomness: Random = Random) = this.matrix(randomness.nextUInt(rowsRange), columns) { _, _ -> ModularUInt(modulus, randomness.nextUInt(modulus)) }
fun RingModularUInt.randomModularUIntMatrix(rowsRange: UIntRange, columnsRange: UIntRange, randomness: Random = Random) = this.matrix(randomness.nextUInt(rowsRange), randomness.nextUInt(columnsRange)) { _, _ -> ModularUInt(modulus, randomness.nextUInt(modulus)) }

/**
 * Fast Dice Roller algorithm by Jérémie Lumbroso
 *
 * n = 2^k, r uniformly distributed in [0, n-1]
 */
fun Random.nextUIntFDR(bound: UInt): UInt {
    if (bound <= Int.MAX_VALUE.toUInt()) {
        val log = bound.ceilLog2()
        var n = 1u shl log
        var r = this.nextBits(log).toUInt()
        while (true) {
            if (n >= bound) {
                if (r < bound) {
                    return r
                } else {
                    n -= bound
                    r -= bound
                }
            }
            n = n.shl(1)
            r = r.shl(1) + this.nextBits(1).toUInt()
        }
    } else {
        var n = twoPower32
        var r = this.nextUInt()
        while (true) {
            if (n >= bound) {
                if (r < bound) {
                    return r
                } else {
                    n -= bound
                    r -= bound
                }
            }
            n = n.shl(1)
            r = r.shl(1) + this.nextBits(1).toUInt()
        }
    }
}

fun Random.nextUIntFDR(from: UInt, until: UInt): UInt {
    require(from < until)
    return from + nextUIntFDR(until - from)
}

fun Random.nextUIntFDR(range: UIntRange): UInt {
    require(range.isEmpty().not())
    return range.first + nextUIntFDR(range.last + 1u - range.first)
}


/**
 * generating random factored number algorithm by Adam Kalai
 *
 * return a uniformly random integer in [1, n] with it's factorization
 *
 * TODO implement a fast isPrime() then remove `suspend`
 */
suspend fun Random.randomFactoredNumber(n: UInt): FactorizationUInt {
    require(n != 0u)
    val factors = mutableListOf<FactorizationUIntPrimePower>()
    var lastPrime = 0u
    var lastPower = 1u  //power may not be too big in general, using manual multiplication permits early rejection and safe multiplication
    var lastPrimePower = 0u
    var r = 1uL     //prevent overflow

    var a = this.nextUIntFDR(1u..n)
    while (a != 1u) {
        if (a.isPrime()) {
            r *= a
            if (r > n) return this.randomFactoredNumber(n)
            if (lastPrime == a) {
                lastPower++
                lastPrimePower *= a
            } else {
                if (lastPrime == 0u) {
                    lastPrime = a
                    lastPrimePower = a
                } else {
                    factors += FactorizationUIntPrimePower(lastPrimePower, lastPrime, lastPower)
                    lastPrime = a
                    lastPower = 1u
                    lastPrimePower = a
                }
            }
        }
        a = this.nextUInt(1u..a)
    }

    return if (this.nextUIntFDR(n) < r) {   //with probability r/n

        if (lastPrime != 0u) {
            factors += FactorizationUIntPrimePower(lastPrimePower, lastPrime, lastPower)
        }
        factors.reverse()
        FactorizationUInt(r.toUInt(), factors)

    } else this.randomFactoredNumber(n)
}