package math.random

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.complex_number.FieldComplexNumberDouble
import math.abstract_structure.instance.FieldDouble
import math.complex_number.ComplexNumber
import math.integer.big_integer.ceilLog2
import math.integer.uint.RingUInt
import math.integer.uint.ceilLog2
import math.integer.uint.factored.*
import math.integer.uint.isPrime
import math.integer.uint.modular.ModularUInt
import math.integer.uint.modular.RingModularUInt
import math.integer.ulong.modular.ModularULong
import math.integer.ulong.modular.RingModularULong
import math.martix.AbstractMatrix
import math.martix.columnVector
import math.martix.concrete.OrdinaryMatrix
import math.martix.concrete.OrdinarySquareMatrix
import math.martix.matrix
import math.martix.squareMatrix
import math.twoPower31
import math.twoPower32
import util.stdlib.shl
import util.stdlib.shr
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.random.nextULong

/**
 * Created by CowardlyLion at 2022/1/12 18:18
 */

//naive implementation of randomization with given weights
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

fun Random.randomMultiplicableUIntMatrices(matrices: UInt, size: UIntRange, bound: UIntRange): List<AbstractMatrix<UInt>> {
    var a = nextUInt(size)
    return List(matrices.toInt()) {
        val b = nextUInt(size)
        val matrix = randomUIntMatrix(a, b, bound)
        a = b
        matrix
    }
}

fun Random.randomUIntMatrices(matrices: UInt, sizeRange: UIntRange, bound: UIntRange): List<AbstractMatrix<UInt>> {
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
fun RingModularUInt.randomColumnVector(size: UInt, randomness: Random = Random) = this.columnVector(size) { ModularUInt(modulus, randomness.nextUInt(modulus)) }

fun RingModularULong.randomMatrix(rowsRange: UIntRange, columnsRange: UIntRange, randomness: Random = Random) = this.matrix(randomness.nextUInt(rowsRange), randomness.nextUInt(columnsRange)) { _, _ -> ModularULong(modulus, randomness.nextULong(modulus)) }
fun RingModularUInt.randomMatrix(rows: UInt, columns: UInt, randomness: Random = Random) = this.matrix(rows, columns) { _, _ -> ModularUInt(modulus, randomness.nextUInt(modulus)) }
fun RingModularUInt.randomMatrix(rows: UInt, columnsRange: UIntRange, randomness: Random = Random) = this.matrix(rows, randomness.nextUInt(columnsRange)) { _, _ -> ModularUInt(modulus, randomness.nextUInt(modulus)) }
fun RingModularUInt.randomMatrix(rowsRange: UIntRange, columns: UInt, randomness: Random = Random) = this.matrix(randomness.nextUInt(rowsRange), columns) { _, _ -> ModularUInt(modulus, randomness.nextUInt(modulus)) }
fun RingModularUInt.randomMatrix(rowsRange: UIntRange, columnsRange: UIntRange, randomness: Random = Random) = this.matrix(randomness.nextUInt(rowsRange), randomness.nextUInt(columnsRange)) { _, _ -> ModularUInt(modulus, randomness.nextUInt(modulus)) }

/**
 * return a random complex number that within range [-bound, bound)
 */
fun Random.randomComplexNumberDouble(bound: Double): ComplexNumber<Double> = ComplexNumber(FieldDouble, nextDouble(-bound, bound), nextDouble(-bound, bound))

fun FieldComplexNumberDouble.randomMatrix(rows: UInt, columns: UInt, bound: Double, randomness: Random = Random): AbstractMatrix<ComplexNumber<Double>> = this.matrix(rows, columns) { _, _ -> randomness.randomComplexNumberDouble(bound) }

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
    if (bound <= twoPower31) {
        val bits = bound.ceilLog2()  //1 <= log <= 31
        var n = 1u shl bits

        val randomUInt = this.nextUInt()        //make every input (including 2^k) has nearly same performance.
        var r = randomUInt.and(n - 1u)

        val randomBit = RandomBit(this, randomUInt shr bits, 32u - bits)

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
            r = r.shl(1) + randomBit.nextBitUInt()
        }
    } else {
        var n = twoPower32
        var r = this.nextUInt()     //make every input (including 2^k) has nearly same performance.

        val randomBit = RandomBit(this)

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
            r = r.shl(1) + randomBit.nextBitUInt()
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
 * Fast Dice Roller algorithm by Jérémie Lumbroso
 *
 * n = 2^k, r uniformly distributed in [0, n-1]
 *
 * faster than [nextUIntFDR]
 *
 * return integer uniformly distributed in [0, [bound]-1]
 */
fun Random.nextBigIntegerFDR(bound: BigInteger): BigInteger {
    require(bound.isPositive)
    if (bound == BigInteger.ONE) return BigInteger.ZERO

    val bits = bound.ceilLog2()  //1 <= log
    require(bits <= Int.MAX_VALUE.toULong())  //only accept "small but large enough" value of bound

    val randomBit = RandomBit(this)

    var n = BigInteger.ONE shl bits.toInt()
    var r = randomBit.nextBigInteger(bits)

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
        r = r.shl(1) + if (randomBit.nextBit()) BigInteger.ONE else BigInteger.ZERO
    }
}

/**
 * generating random factored number algorithm by Adam Kalai
 *
 * return a uniformly random integer in [1, n] with it's factorization
 *
 * TODO implement a fast isPrime() then remove `suspend`
 */
suspend fun nextFactoredNumberFDR(n: UInt, nextUIntMethod: (UIntRange) -> UInt): UIntPPPI {
    require(n != 0u)
    rerun@ while (true) {

        val factors = mutableListOf<UIntPPI>()
        var lastPrime = 0u
        var lastPower = 1u  //power may not be too big in general, using manual multiplication permits early rejection and safe multiplication
//        var lastPrimePower = 0u //use fast power method
        var r = 1uL     //prevent overflow

        var a = nextUIntMethod(1u..n)
        while (a != 1u) {
            if (a.isPrime()) {
                r *= a
                if (r > n) continue@rerun
                when (lastPrime) {
                    a    -> lastPower++
                    0u   -> lastPrime = a
                    else -> {
                        factors += ofPrimePower(lastPrime, lastPower)
                        lastPrime = a
                        lastPower = 1u
                    }
                }
            }
            a = nextUIntMethod(1u..a)
        }

        if (nextUIntMethod(0u until n) < r) {   //accept r with probability r/n
            if (lastPrime != 0u) {
                factors += ofPrimePower(lastPrime, lastPower)
            }
            factors.reverse()
            return ofPrimePowers(factors, r.toUInt())
        }
    }
}

suspend fun Random.nextFactoredNumberFDR(n: UInt): UIntPPPI = nextFactoredNumberFDR(n) { range -> nextUIntFDR(range) }

suspend fun Random.nextFactoredNumber(n: UInt): UIntPPPI = nextFactoredNumberFDR(n) { range -> nextUInt(range) }
