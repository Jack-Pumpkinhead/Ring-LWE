package math.martix.tensor

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.Ring
import math.abstract_structure.instance.RingBigInteger
import math.abstract_structure.instance.RingUInt
import math.canMultiplyElementWise
import math.coding.LadderIndex
import math.martix.AbstractMatrix
import math.martix.FormalProduct
import math.martix.decomposeFormalKroneckerProduct
import math.operation.product
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/8 20:27
 */
class FormalKroneckerProduct<A>(ring: Ring<A>, val elements: List<AbstractMatrix<A>>) : FormalProduct<A>(ring, ring.decomposeFormalKroneckerProduct(elements)) {

    val rowIndex: LadderIndex
    val columnIndex: LadderIndex

    init {
        val rows = elements.map { it.rows }
        rowIndex = LadderIndex(rows, RingUInt.product(rows))
        val columns = elements.map { it.columns }
        columnIndex = LadderIndex(columns, RingUInt.product(columns))

        lazyAssert2 {
            assert(elements.isNotEmpty())
            val totalRows = RingBigInteger.product(rows.map { it.toBigInteger() })
            assert(totalRows <= UInt.MAX_VALUE)
            val totalColumns = RingBigInteger.product(columns.map { it.toBigInteger() })
            assert(totalColumns <= UInt.MAX_VALUE)
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        val row1 = rowIndex.decode(row)
        val column1 = columnIndex.decode(column)
        return ring.product(0u until elements.size.toUInt()) { i -> elements[i.toInt()].elementAt(row1[i.toInt()], column1[i.toInt()]) }
    }


    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when {
        matrix is FormalKroneckerProduct<A>
                && canMultiplyElementWise(this.elements, matrix.elements) -> {   //It's possible to implement a more intelligent mix-tensor-product resolution method, based on string diagrams of monoidal category.
            FormalKroneckerProduct(ring, elements.zip(matrix.elements).map { (a, b) -> a * b })
        }
        else                                                              -> super.timesImpl(matrix)
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when {
        matrix is FormalKroneckerProduct<A>
                && canMultiplyElementWise(this.elements, matrix.elements) -> {   //It's possible to implement a more intelligent mix-tensor-product resolution method.
            FormalKroneckerProduct(ring, elements.zip(matrix.elements).map { (a, b) -> a.timesRowParallel(b) })
        }
        else                                                              -> super.timesRowParallelImpl(matrix)
    }


    override fun downCast(): AbstractMatrix<A> = when (elements.size) {
        1    -> elements[0]
        else -> this
    }
}