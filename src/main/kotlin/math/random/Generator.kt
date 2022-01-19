package math.random

import math.abstract_structure.Ring
import math.abstract_structure.instance.ringModularUInt
import math.abstract_structure.instance.ringUInt
import math.integer.modular.UIntModular
import math.integer.modular.ULongModular
import math.martix.concrete.OrdinaryMatrix
import math.martix.matrix
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

fun Random.randomUIntMatrix(rows: UInt, columns: UInt, bound: UIntRange) = ringUInt.matrix(rows, columns) { _, _ -> nextUInt(bound) }
fun Random.randomUIntMatrix(rowsRange: UIntRange, columnsRange: UIntRange, bound: UIntRange) = ringUInt.matrix(nextUInt(rowsRange), nextUInt(columnsRange)) { _, _ -> nextUInt(bound) }
fun Random.randomMultiplicableUIntMatrices(matrices: UInt, size: UIntRange, bound: UIntRange): List<OrdinaryMatrix<UInt>> {
    var a = nextUInt(size)
    return List(matrices.toInt()) {
        val b = nextUInt(size)
        val matrix = randomUIntMatrix(a, b, bound)
        a = b
        matrix
    }
}

fun Random.randomUIntMatrices(matrices: UInt, size: UIntRange, bound: UIntRange): List<OrdinaryMatrix<UInt>> {
    return List(matrices.toInt()) {
        randomUIntMatrix(nextUInt(size), nextUInt(size), bound)
    }
}

fun Random.randomModularUIntMatrix(modulus: UInt, rowsRange: UIntRange, columnsRange: UIntRange) = ringModularUInt(modulus).matrix(nextUInt(rowsRange), nextUInt(columnsRange)) { _, _ -> UIntModular(modulus, nextUInt(modulus)) }
fun Random.randomModularULongMatrix(ring: Ring<ULongModular>, modulus: ULong, rowsRange: UIntRange, columnsRange: UIntRange) = ring.matrix(nextUInt(rowsRange), nextUInt(columnsRange)) { _, _ -> ULongModular(modulus, nextULong(modulus)) }
