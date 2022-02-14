package math.martix.tensor

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.Ring
import math.integer.big_integer.RingBigInteger
import math.coding.LadderIndex
import math.martix.*
import math.operation.product
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/8 20:27
 *
 * formal kronecker product modeled as multiplicative chain of whiskered matrices (with elements[0] at right-most)
 *
 * require [elements] is not empty
 */
class FormalKroneckerProduct<A>(override val ring: Ring<A>, override val rows: UInt, override val columns: UInt, override val elements: List<AbstractMatrix<A>>) : AbstractFormalProduct<A>, AbstractFormalKroneckerProduct<A> {

    //there are (matrices.size)! ways (permutations) of decomposition, use one that compute m0 first.
    override val matrices = when (elements.size) {
        1    -> elements
        else -> {
            var l = rows
            var r = 1u

            val result = mutableListOf<AbstractMatrix<A>>()
            for (i in elements.size - 1 downTo 0) {
                val m = elements[i]
                l /= m.rows
                val lr = l * r
                result += WhiskeredKroneckerProduct(ring, lr * m.rows, lr * m.columns, l, m, r)
                r *= m.columns
            }
            result
        }
    }

    override val rowIndex: LadderIndex = LadderIndex(elements.map { it.rows }, rows)

    override val columnIndex: LadderIndex = LadderIndex(elements.map { it.columns }, columns)

    init {
        lazyAssert2 {
            assert(elements.isNotEmpty())
            assert(rows.toBigInteger() == RingBigInteger.product(elements.map { it.rows.toBigInteger() }))
            assert(columns.toBigInteger() == RingBigInteger.product(elements.map { it.columns.toBigInteger() }))
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        val row1 = rowIndex.decode(row)
        val column1 = columnIndex.decode(column)
        return ring.product(0u until elements.size.toUInt()) { i -> elements[i.toInt()].elementAtUnsafe(row1[i.toInt()], column1[i.toInt()]) }
    }

    override fun downCast(): AbstractMatrix<A> = when (elements.size) {
        1    -> elements[0]
        else -> this
    }

}