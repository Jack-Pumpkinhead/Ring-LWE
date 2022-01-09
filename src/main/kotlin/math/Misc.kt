package math

import math.abstract_structure.instance.ringUInt
import math.martix.AbstractMatrix
import math.martix.OrdinaryMatrix
import math.martix.matrix
import kotlin.random.Random
import kotlin.random.nextUInt

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

fun <R, T> List<T>.runningFoldRight(initial: R, operation: (acc: R, T) -> R): List<R> {
    if (isEmpty()) return listOf(initial)
    val result = ArrayList<R>(size + 1).apply { add(initial) }
    var accumulator = initial
    this.reversed()
    this.asReversed()
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

fun <A> diagonalBlockSizeEquals(a: List<AbstractMatrix<A>>, b: List<AbstractMatrix<A>>): Boolean {
    if (a.size != b.size) return false
    for (i in a.indices) {
        if (a[i].rows != b[i].rows) {
            return false
        }
    }
    return true
}

fun randomUIntMatrix(rows: UInt, columns: UInt, bound: UIntRange) = ringUInt.matrix(rows, columns) { _, _ -> Random.nextUInt(bound) }
fun randomUIntMatrix(rowsRange: UIntRange, columnsRange: UIntRange, bound: UIntRange) = ringUInt.matrix(Random.nextUInt(rowsRange), Random.nextUInt(columnsRange)) { _, _ -> Random.nextUInt(bound) }
fun randomMultiplicableUIIntMatrix(matrices: UInt, size: UIntRange, bound: UIntRange): List<OrdinaryMatrix<UInt>> {
    var a = Random.nextUInt(size)
    return List(matrices.toInt()) {
        val b = Random.nextUInt(size)
        val matrix = randomUIntMatrix(a, b, bound)
        a = b
        matrix
    }
}

val debug = false

fun <A> AbstractMatrix<A>.andPrint(): AbstractMatrix<A> {
    if (debug) {
        println("$this\n\n")
    }
    return this
}