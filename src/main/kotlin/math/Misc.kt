package math

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.instance.ringUInt
import math.integer.gcd
import math.martix.AbstractMatrix
import math.martix.concrete.OrdinaryMatrix
import math.martix.matrix
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * Created by CowardlyLion at 2022/1/8 20:56
 */

fun <A> sizeOfFirstRowOrZero(matrix: List<List<A>>): UInt {
    return if (matrix.isEmpty()) 0u else matrix[0].size.toUInt()
}

fun <A> requireEqualSize(matrix: List<List<A>>) {
    if (matrix.isNotEmpty()) {
        val columns = matrix[0].size
        for (row in matrix) {
            require(row.size == columns)
        }
    }
}

/**
 * delete this when kotlin stdlib have this method.
 * */
fun <R, T> List<T>.runningFoldRight(initial: R, operation: (acc: R, T) -> R): List<R> {
    if (isEmpty()) return listOf(initial)
    val result = ArrayList<R>(size + 1).apply { add(initial) }
    var accumulator = initial
    for (i in this.indices.reversed()) {
        accumulator = operation(accumulator, this[i])
        result.add(accumulator)
    }
    return result.reversed()
}

inline fun <T> list(size: UInt, init: (index: UInt) -> T): List<T> {
    val list = ArrayList<T>(size.toInt())
    for (i in 0u until size) {
        list.add(init(i))
    }
    return list
}

fun <A> canMultiplyElementWise(a: List<AbstractMatrix<A>>, b: List<AbstractMatrix<A>>): Boolean {
    if (a.size != b.size) return false
    for (i in a.indices) {
        if (a[i].columns != b[i].rows) {
            return false
        }
    }
    return true
}

fun randomUIntMatrix(rows: UInt, columns: UInt, bound: UIntRange) = ringUInt.matrix(rows, columns) { _, _ -> Random.nextUInt(bound) }
fun randomUIntMatrix(rowsRange: UIntRange, columnsRange: UIntRange, bound: UIntRange) = ringUInt.matrix(Random.nextUInt(rowsRange), Random.nextUInt(columnsRange)) { _, _ -> Random.nextUInt(bound) }
fun randomMultiplicableUIntMatrices(matrices: UInt, size: UIntRange, bound: UIntRange): List<OrdinaryMatrix<UInt>> {
    var a = Random.nextUInt(size)
    return List(matrices.toInt()) {
        val b = Random.nextUInt(size)
        val matrix = randomUIntMatrix(a, b, bound)
        a = b
        matrix
    }
}

fun randomUIntMatrices(matrices: UInt, size: UIntRange, bound: UIntRange): List<OrdinaryMatrix<UInt>> {
    return List(matrices.toInt()) {
        randomUIntMatrix(Random.nextUInt(size), Random.nextUInt(size), bound)
    }
}


val debug = false

fun <A> AbstractMatrix<A>.andPrint(): AbstractMatrix<A> {
    if (debug) {
        println("$this\n\n")
    }
    return this
}

@OptIn(ExperimentalTime::class)
inline fun measureTimeAndPrint(name: String, block: () -> Unit) {
    val time = measureTime(block)

    println("$name: ${time.toString(DurationUnit.MILLISECONDS, 5)}")
}

/**
 * possibly contain 1
 * if list contain 0, then it consisted of 0, 1 only.
 * */
fun List<UInt>.isPairwiseCoprimeUInt(): Boolean {
    if (isEmpty() || size == 1) return true
    for (i in 0..size - 2) {
        for (j in i + 1 until size) {
            if (gcd(this[i], this[j]) != 1u) return false
        }
    }
    return true
}

/**
 * possibly contain ±1
 * if list contain 0, then it consisted of 0, 1 only.
 * */
fun List<BigInteger>.isPairwiseCoprimeBigInteger(): Boolean {
    if (isEmpty() || size == 1) return true
    for (i in 0..size - 2) {
        for (j in i + 1 until size) {
            if (this[i].gcd(this[j]) != BigInteger.ONE) return false
        }
    }
    return true
}


fun nonNegMin(a: BigInteger, b: ULong): ULong {
    require(!a.isNegative)
    return if (a > b.toBigInteger()) b else a.ulongValue()
}

fun ULong.bitAt(i: UInt): Boolean {
    require(i < 64u)
    return this.shr(i.toInt()).and(1uL) == 1uL
}

fun ULong.setBitAt(i: UInt, bit: Boolean): ULong {
    require(i < 64u)
    return if (bit) {
        this.or(1uL.shl(i.toInt()))
    } else {
        this.inv().or(1uL.shl(i.toInt())).inv()
    }
}
