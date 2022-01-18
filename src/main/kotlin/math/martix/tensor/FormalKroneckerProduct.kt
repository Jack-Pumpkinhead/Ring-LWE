package math.martix.tensor

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.Ring
import math.canMultiplyElementWise
import math.coding.LadderIndex
import math.martix.AbstractMatrix
import math.martix.FormalProduct
import math.martix.decomposeFormalKroneckerProduct
import math.operation.product

/**
 * Created by CowardlyLion at 2022/1/8 20:27
 */
class FormalKroneckerProduct<A>(ring: Ring<A>, val elements: List<AbstractMatrix<A>>) : FormalProduct<A>(ring, ring.decomposeFormalKroneckerProduct(elements)) {

    val rowIndex = LadderIndex(elements.map { it.rows.toBigInteger() })
    val columnIndex = LadderIndex(elements.map { it.columns.toBigInteger() })

    init {
        require(rowIndex.indexBound <= UInt.MAX_VALUE)
        require(columnIndex.indexBound <= UInt.MAX_VALUE)
//        empty elements is ruled out in FormalProduct
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        val rowIndices = rowIndex.decode(row.toBigInteger()).map { it.uintValue() }
        val columnIndices = columnIndex.decode(column.toBigInteger()).map { it.uintValue() }
        return ring.product(0u until elements.size.toUInt()) { i -> elements[i.toInt()].elementAt(rowIndices[i.toInt()], columnIndices[i.toInt()]) }
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