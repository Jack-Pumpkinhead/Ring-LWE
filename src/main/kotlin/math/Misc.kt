package math

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.integer.gcd
import math.martix.AbstractMatrix
import kotlin.math.PI
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * Created by CowardlyLion at 2022/1/8 20:56
 */

fun <A> sizeOfFirstRowOrZero(matrix: List<List<A>>): UInt {
    return if (matrix.isEmpty()) 0u else matrix[0].size.toUInt()
}

fun <A> isRectangular(matrix: List<List<A>>): Boolean {
    if (matrix.isNotEmpty()) {
        val columns = matrix[0].size
        for (row in matrix) {
            if (row.size != columns) return false
        }
    }
    return true
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


val enableAssertion = true
val printMatrix = false
val roundingErrorDouble = 0.3

fun <A> AbstractMatrix<A>.andPrint(info: String = ""): AbstractMatrix<A> {
    if (printMatrix) {
        println("$info\t$this\n\n")
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

fun UInt.bitAt(i: UInt): Boolean {
    require(i < 32u)
    return this.shr(i.toInt()).and(1u) == 1u
}

fun UInt.setBitAt(i: UInt, bit: Boolean): UInt {
    require(i < 32u)
    return if (bit) {
        this.or(1u.shl(i.toInt()))
    } else {
        this.inv().or(1u.shl(i.toInt())).inv()
    }
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

val twoPower32 = 1uL.shl(32)
val twoPower64 = BigInteger.ONE.shl(64)

const val pi2 = PI * 2