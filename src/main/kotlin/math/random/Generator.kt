package math.random

import math.abstract_structure.instance.RingModularUInt
import math.abstract_structure.instance.RingModularULong
import math.abstract_structure.instance.RingUInt
import math.integer.modular.ModularUInt
import math.integer.modular.ModularULong
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

fun Random.randomUIntMatrix(rows: UInt, columns: UInt, bound: UIntRange) = RingUInt.matrix(rows, columns) { _, _ -> nextUInt(bound) }
fun Random.randomUIntMatrix(rowsRange: UIntRange, columnsRange: UIntRange, bound: UIntRange) = RingUInt.matrix(nextUInt(rowsRange), nextUInt(columnsRange)) { _, _ -> nextUInt(bound) }
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

fun RingModularULong.randomModularULongMatrix(rowsRange: UIntRange, columnsRange: UIntRange, randomness: Random = Random) = this.matrix(randomness.nextUInt(rowsRange), randomness.nextUInt(columnsRange)) { _, _ -> ModularULong(modulus, randomness.nextULong(modulus)) }
fun RingModularUInt.randomModularUIntMatrix(rowsRange: UIntRange, columnsRange: UIntRange, randomness: Random = Random) = this.matrix(randomness.nextUInt(rowsRange), randomness.nextUInt(columnsRange)) { _, _ -> ModularUInt(modulus, randomness.nextUInt(modulus)) }
