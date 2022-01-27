package math.martix.tensor

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.Ring
import math.abstract_structure.instance.RingBigInteger
import math.abstract_structure.instance.RingUInt
import math.coding.LadderIndex
import math.martix.AbstractSquareMatrix
import math.martix.decomposeSquareFormalKroneckerProduct
import math.operation.product
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/27 16:40
 */
class SquareFormalKroneckerProduct<A>(ring: Ring<A>, val elements: List<AbstractSquareMatrix<A>>) : SquareFormalProduct<A>(ring, ring.decomposeSquareFormalKroneckerProduct(elements)) {

    val index: LadderIndex

    init {
        val rows = elements.map { it.rows }
        index = LadderIndex(rows, RingUInt.product(rows))

        lazyAssert2 {
            assert(elements.isNotEmpty())
            val totalRows = RingBigInteger.product(rows.map { it.toBigInteger() })
            assert(totalRows <= UInt.MAX_VALUE)
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        val row1 = index.decode(row)
        val column1 = index.decode(column)
        return ring.product(0u until elements.size.toUInt()) { i -> elements[i.toInt()].elementAt(row1[i.toInt()], column1[i.toInt()]) }
    }


//    over checking may slow
    /*override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when {
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
    }*/


    override fun downCast(): AbstractSquareMatrix<A> = when (elements.size) {
        1    -> elements[0]
        else -> this
    }
}