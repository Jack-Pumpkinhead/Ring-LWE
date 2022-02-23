package math.operation

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.Ring
import math.complex_number.ComplexNumber
import math.complex_number.maxRoundingError
import math.martix.AbstractMatrix
import math.martix.matrix
import math.martix.matrixRowParallel
import math.martix.mutable.AbstractMutableMatrix
import math.martix.mutable.MutableMatrix
import math.martix.zeroMutableMatrix
import math.roundingErrorDouble
import util.stdlib.list
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Created by CowardlyLion at 2022/1/9 18:15
 */

fun <A> multiply(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>): AbstractMatrix<A> {
    require(mA.ring == mB.ring)
    require(mA.columns == mB.rows)
    return mA.ring.multiplyUnsafe(mA, mB)
}

/**
 * Used as base for testing correctness of other (optimized) matrix multiplication method.
 * */
fun <A> Ring<A>.multiplyUnsafe(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>): AbstractMatrix<A> {
    return matrix(mA.rows, mB.columns) { i, k ->
        var sum = zero
        for (j in 0u until mA.columns) {
            sum = add(sum, multiply(mA.elementAtUnsafe(i, j), mB.elementAtUnsafe(j, k)))
        }
        sum
    }.downCast()
}

fun <A> multiplyToNewMutableMatrix(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>): MutableMatrix<A> {
    require(mA.ring == mB.ring)
    require(mA.columns == mB.rows)
    val result = mA.ring.zeroMutableMatrix(mA.rows, mB.columns)
    mA.ring.multiplyToUnsafe(mA, mB, result)
    return result
}

fun <A> multiplyTo(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
    require(mA.ring == mB.ring)
    require(mA.ring == dest.ring)
    require(mA.columns == mB.rows)
    require(mA.rows == dest.rows)
    require(mB.columns == dest.columns)
    mA.ring.multiplyToUnsafe(mA, mB, dest)
}

fun <A> Ring<A>.multiplyToUnsafe(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
    for (i in 0u until dest.rows) {
        for (k in 0u until dest.columns) {
            var sum = zero
            for (j in 0u until mA.columns) {
                sum = add(sum, multiply(mA.elementAtUnsafe(i, j), mB.elementAtUnsafe(j, k)))
            }
            dest.setElementAt(i, k, sum)
        }
    }
}

suspend fun <A> multiplyRowParallel(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>): AbstractMatrix<A> {
    require(mA.ring == mB.ring)
    require(mA.columns == mB.rows)
    return mA.ring.multiplyRowParallelUnsafe(mA, mB).downCast()
}

suspend fun <A> Ring<A>.multiplyRowParallelUnsafe(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>): AbstractMatrix<A> {
    return matrixRowParallel(mA.rows, mB.columns) { i, k ->
        var sum = zero
        for (j in 0u until mA.columns) {
            sum = add(sum, multiply(mA.elementAtUnsafe(i, j), mB.elementAtUnsafe(j, k)))    //maybe faster by caching matrix[i][j], but improvement on speed may insignificant.
        }
        sum
    }.downCast()
}

suspend fun <A> multiplyToNewMutableMatrixRowParallel(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>): MutableMatrix<A> {
    require(mA.ring == mB.ring)
    require(mA.columns == mB.rows)
    val result = mA.ring.zeroMutableMatrix(mA.rows, mB.columns)
    mA.ring.multiplyToRowParallelUnsafe(mA, mB, result)
    return result
}

suspend fun <A> multiplyToRowParallel(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
    require(mA.ring == mB.ring)
    require(mA.columns == mB.rows)
    mA.ring.multiplyToRowParallelUnsafe(mA, mB, dest)
}

/**
 * By structural concurrency of coroutineScope, no need to joinAll() launched task.
 * Concurrently setting the value in an array would not throw ConcurrentModificationException
 * */
suspend fun <A> Ring<A>.multiplyToRowParallelUnsafe(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) = coroutineScope {
    for (i in 0u until dest.rows) {
        launch {
            for (k in 0u until dest.columns) {
                var sum = zero
                for (j in 0u until mA.columns) {
                    sum = add(sum, multiply(mA.elementAtUnsafe(i, j), mB.elementAtUnsafe(j, k)))
                }
                dest.setElementAt(i, k, sum)
            }
        }
    }
}

fun <A> matrixEquals(m1: AbstractMatrix<A>, m2: AbstractMatrix<A>): Boolean {
    if (m1 === m2) return true

    if (m1.ring != m2.ring) return false
    if (m1.rows != m2.rows) return false
    if (m1.columns != m2.columns) return false
    for (i in 0u until m1.rows) {
        for (j in 0u until m1.columns) {
            if (m1.elementAtUnsafe(i, j) != m2.elementAtUnsafe(i, j)) return false
        }
    }
    return true
}

fun matrixApproximatelyEquals(m1: AbstractMatrix<ComplexNumber<Double>>, m2: AbstractMatrix<ComplexNumber<Double>>): Boolean {
    if (m1 === m2) return true

    if (m1.ring != m2.ring) return false
    if (m1.rows != m2.rows) return false
    if (m1.columns != m2.columns) return false
    for (i in 0u until m1.rows) {
        for (j in 0u until m1.columns) {
            val a1 = m1.elementAtUnsafe(i, j)
            val a2 = m2.elementAtUnsafe(i, j)
            val diff = a1 - a2
            maxRoundingError = max(maxRoundingError, diff.real)
            maxRoundingError = max(maxRoundingError, diff.imaginary)
            if (diff.real > roundingErrorDouble || diff.imaginary > roundingErrorDouble) {
                return false
            }
        }
    }
    return true
}

fun <A> matrixHashCode(matrix: AbstractMatrix<A>): Int {
    var result = matrix.ring.hashCode()
    result = 31 * result + matrix.rows.hashCode()
    result = 31 * result + matrix.columns.hashCode()
    for (i in 0u until matrix.rows) {
        for (j in 0u until matrix.columns) {
            result = 31 * result + matrix.elementAtUnsafe(i, j).hashCode()
        }
    }
    return result
}

/**
 * compatible with Mathematica's matrix notation
 * */
fun <A> matrixToString(matrix: AbstractMatrix<A>): String =
    list(matrix.rows) { i ->
        list(matrix.columns) { j ->
            matrix.elementAtUnsafe(i, j)
        }
    }.joinToString(",\n", "{\n", "}") { row ->
        row.joinToString(", ", "{", "}") { it.toString() }
    }

fun maxAbsoluteDistance(m1: AbstractMatrix<ComplexNumber<Double>>, m2: AbstractMatrix<ComplexNumber<Double>>): Double {
    require(m1.rows == m2.rows)
    require(m1.columns == m2.columns)

    var maxDistance = 0.0
    m1.indexed { i, j ->
        val distance = sqrt((m1.elementAtUnsafe(i, j) - m2.elementAtUnsafe(i, j)).lengthSquared())
        if (distance > maxDistance) {
            maxDistance = distance
        }
    }
    return maxDistance
}