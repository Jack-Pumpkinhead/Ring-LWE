package math.martix.tensor

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.Ring
import math.abstract_structure.instance.ringBigInteger
import math.canMultiplyElementWise
import math.coding.BigLadderIndex
import math.martix.AbstractMatrix
import math.martix.FormalProduct
import math.martix.decomposeFormalKroneckerProduct
import math.operation.product

/**
 * Created by CowardlyLion at 2022/1/8 20:27
 */
class FormalKroneckerProduct<A>(ring: Ring<A>, val elements: List<AbstractMatrix<A>>) : FormalProduct<A>(ring, ring.decomposeFormalKroneckerProduct(elements)) {

    val rowIndex: BigLadderIndex    //TODO make index small
    val columnIndex: BigLadderIndex

    init {
        val rows = elements.map { it.rows.toBigInteger() }
        rowIndex = BigLadderIndex(rows, ringBigInteger.product(rows))
        val columns = elements.map { it.columns.toBigInteger() }
        columnIndex = BigLadderIndex(columns, ringBigInteger.product(columns))
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