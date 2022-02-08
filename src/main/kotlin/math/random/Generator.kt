package math.random

import math.abstract_structure.instance.*
import math.complex_number.ComplexNumber
import math.integer.*
import math.integer.modular.ModularUInt
import math.integer.modular.ModularULong
import math.martix.concrete.OrdinaryMatrix
import math.martix.concrete.OrdinarySquareMatrix
import math.martix.matrix
import math.martix.squareMatrix
import math.twoPower32
import util.stdlib.shr
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

fun RingModularULong.randomMatrix(rowsRange: UIntRange, columnsRange: UIntRange, randomness: Random = Random) = this.matrix(randomness.nextUInt(rowsRange), randomness.nextUInt(columnsRange)) { _, _ -> ModularULong(modulus, randomness.nextULong(modulus)) }
fun RingModularUInt.randomMatrix(rows: UInt, columns: UInt, randomness: Random = Random) = this.matrix(rows, columns) { _, _ -> ModularUInt(modulus, randomness.nextUInt(modulus)) }
fun RingModularUInt.randomMatrix(rows: UInt, columnsRange: UIntRange, randomness: Random = Random) = this.matrix(rows, randomness.nextUInt(columnsRange)) { _, _ -> ModularUInt(modulus, randomness.nextUInt(modulus)) }
fun RingModularUInt.randomMatrix(rowsRange: UIntRange, columns: UInt, randomness: Random = Random) = this.matrix(randomness.nextUInt(rowsRange), columns) { _, _ -> ModularUInt(modulus, randomness.nextUInt(modulus)) }
fun RingModularUInt.randomMatrix(rowsRange: UIntRange, columnsRange: UIntRange, randomness: Random = Random) = this.matrix(randomness.nextUInt(rowsRange), randomness.nextUInt(columnsRange)) { _, _ -> ModularUInt(modulus, randomness.nextUInt(modulus)) }

/**
 * return a random complex number that within range [-bound, bound)
 */
fun Random.randomComplexNumberDouble(bound: Double): ComplexNumber<Double> = ComplexNumber(FieldDouble, nextDouble(-bound, bound), nextDouble(-bound, bound))

fun FieldComplexNumberDouble.randomMatrix(rows: UInt, columns: UInt, bound: Double, randomness: Random = Random): OrdinaryMatrix<ComplexNumber<Double>> = this.matrix(rows, columns) { _, _ -> randomness.randomComplexNumberDouble(bound) }

/**
 * Fast Dice Roller algorithm by Jérémie Lumbroso
 *
 * n = 2^k, r uniformly distributed in [0, n-1]
 *
 * return integer uniformly distributed in [0, [bound]-1]
 */
fun Random.nextUIntFDR(bound: UInt): UInt {
    require(bound != 0u)
    if (bound == 1u) return 0u
    if (bound <= Int.MAX_VALUE.toUInt()) {
        val log = bound.ceilLog2()  //log>=1
        var n = 1u shl log

        var randomUInt = this.nextUInt()        //make every input (including 2^k) has nearly same performance.
        var r = randomUInt.mod(n)
        randomUInt = randomUInt shr n
        var bits = 32u - n

        fun nextBit(): UInt =
            if (bits != 0u) {
                val result = randomUInt.mod(2u)
                randomUInt = randomUInt shr 1
                bits--
                result
            } else {
                randomUInt = this.nextUInt()
                val result = randomUInt.mod(2u)
                randomUInt = randomUInt shr 1
                bits = 31u
                result
            }
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
            r = r.shl(1) + nextBit()
        }
    } else {
        var n = twoPower32
        var r = this.nextUInt()

        var randomUInt = 0u        //make every input (including 2^k) has nearly same performance.
        var bits = 0u
        fun nextBit(): UInt =
            if (bits != 0u) {
                val result = randomUInt.mod(2u)
                randomUInt = randomUInt shr 1
                bits--
                result
            } else {
                randomUInt = this.nextUInt()
                val result = randomUInt.mod(2u)
                randomUInt = randomUInt shr 1
                bits = 31u
                result
            }

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
            r = r.shl(1) + nextBit()
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
suspend fun Random.randomFactoredNumberFDR(n: UInt): FactorizationUInt {
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
            if (r > n) return this.randomFactoredNumberFDR(n)
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
        a = this.nextUIntFDR(1u..a)
    }

    return if (this.nextUIntFDR(n) < r) {   //with probability r/n

        if (lastPrime != 0u) {
            factors += FactorizationUIntPrimePower(lastPrimePower, lastPrime, lastPower)
        }
        factors.reverse()
        FactorizationUInt(r.toUInt(), factors)

    } else this.randomFactoredNumberFDR(n)
}

/**
 * generating random factored number algorithm by Adam Kalai
 *
 * return a uniformly random integer in 1..n with it's factorization
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

    var a = this.nextUInt(1u..n)
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

    return if (this.nextUInt(n) < r) {   //with probability r/n

        if (lastPrime != 0u) {
            factors += FactorizationUIntPrimePower(lastPrimePower, lastPrime, lastPower)
        }
        factors.reverse()
        FactorizationUInt(r.toUInt(), factors)

    } else this.randomFactoredNumber(n)
}
