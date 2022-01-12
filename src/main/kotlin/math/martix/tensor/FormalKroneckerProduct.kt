package math.martix.tensor

import math.abstract_structure.CRing
import math.coding.BoundedMultiIndex
import math.canMultiplyElementWise
import math.martix.AbstractMatrix
import math.martix.concrete.Constant
import math.martix.zeroMutableSizeMatrix
import math.operations.product

/**
 * Created by CowardlyLion at 2022/1/8 20:27
 */
open class FormalKroneckerProduct<A>(ring: CRing<A>, val matrices: List<AbstractMatrix<A>>) : AbstractMatrix<A>(ring, matrices.fold(1u) { acc, m -> acc * m.rows }, matrices.fold(1u) { acc, m -> acc * m.columns }) {

    val rowMultiIndex = BoundedMultiIndex(matrices.map { it.rows })
    val columnMultiIndex = BoundedMultiIndex(matrices.map { it.columns })

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        val rowIndices = rowMultiIndex.toMultiIndex(row)
        val columnIndices = columnMultiIndex.toMultiIndex(column)
        return ring.product(0u until matrices.size.toUInt()) { i -> matrices[i.toInt()].elementAt(rowIndices[i.toInt()], columnIndices[i.toInt()]) }
    }


    //    there are (matrices.size)! permutations of decomposition, use one that compute m0 first.
    fun whiskeredTensorAt(i: UInt): WhiskeredKroneckerProduct<A> {
        val r = columnMultiIndex.indexBase[i.toInt()]
        val matrix = matrices[i.toInt()]
        val l = if (i == 0u) 1u else {
            rows / (rowMultiIndex.indexBase[(i - 1u).toInt()])
        }
        return WhiskeredKroneckerProduct(ring, l, matrix, r)
    }

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when {
        matrices.isEmpty()                                                -> super.timesImpl(matrix)   //empty tensor product is 1*1 matrix (id_A)
        matrices.size == 1                                                -> matrices[0].times(matrix)
        matrix is FormalKroneckerProduct<A>
                && canMultiplyElementWise(this.matrices, matrix.matrices) -> {   //It's possible to implement a more intelligent mix-tensor-product resolution method.
            FormalKroneckerProduct(ring, matrices.zip(matrix.matrices).map { (a, b) -> a * b })
        }
        else                                                              -> {
            val m0 = whiskeredTensorAt(0u)
            var a = ring.zeroMutableSizeMatrix(m0.rows, matrix.columns)     //it's possible to make first two matrix multiplication 2x faster (directly create a matrix with result).
            m0.multiplyTo(matrix, a)
            /*val m1 = whiskeredTensorAt(1u)
            var b = ring.zeroMutableSizeMatrix(m1.rows, matrix.columns)
            m1.multiplyTo(a, b)*/
            for (i in 1u until matrices.size.toUInt()) {
                val m = whiskeredTensorAt(i)
                val c = ring.zeroMutableSizeMatrix(m.rows, matrix.columns)      //TODO fix bugs that appeared in the code below, and replace this.
                m.multiplyTo(a, c)
                a = c
            }
            a.shrinkRow(this.rows)
            /*m1.multiplyTo(a, b)
            var storedInA = true
            for (i in 2u until matrices.size.toUInt()) {
                val m = whiskeredTensorAt(i)
                if (storedInA) {
                    a = a.expandRow(m.rows)
                    math.operations.multiplyTo(m, b, a)
//                    m.multiplyTo(b, a)
                } else {
                    b = b.expandRow(m.rows)
                    math.operations.multiplyTo(m, a, b)
//                    m.multiplyTo(a, b)
                }
                storedInA = !storedInA
            }
            val result = if (storedInA) b else a
            result.shrinkRow(this.rows)*/
        }
    }

    override fun downCast(): AbstractMatrix<A> = when {
        matrices.isEmpty() -> Constant(ring, ring.one)
        matrices.size == 1 -> matrices[0]
        else               -> this
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when {
        matrices.isEmpty()                                                -> super.timesRowParallelImpl(matrix)   //empty tensor product is 1*1 matrix (id_A)
        matrices.size == 1                                                -> matrices[0].times(matrix)
        matrix is FormalKroneckerProduct<A>
                && canMultiplyElementWise(this.matrices, matrix.matrices) -> {   //It's possible to implement a more intelligent mix-tensor-product resolution method.
            FormalKroneckerProduct(ring, matrices.zip(matrix.matrices).map { (a, b) -> a.timesRowParallel(b) })
        }
        else                                                              -> {
            val m0 = whiskeredTensorAt(0u)
            var a = ring.zeroMutableSizeMatrix(m0.rows, matrix.columns)     //it's possible to make first two matrix multiplication 2x faster (directly create a matrix with result).
            m0.multiplyToParallel(matrix, a)
            for (i in 1u until matrices.size.toUInt()) {
                val m = whiskeredTensorAt(i)
                val c = ring.zeroMutableSizeMatrix(m.rows, matrix.columns)      //TODO fix bugs that appeared in the code below, and replace this.
                m.multiplyToParallel(a, c)
                a = c
            }
            a.shrinkRow(this.rows)
            /*val m1 = whiskeredTensorAt(1u)
            var b = ring.zeroMutableSizeMatrix(m1.rows, matrix.columns)
            m1.multiplyToParallel(a, b)
            var storedInA = true
            for (i in 2u until matrices.size.toUInt()) {
                val m = whiskeredTensorAt(i)
                if (storedInA) {
                    a = a.expandRow(m.rows)
                    m.multiplyToParallel(b, a)
                } else {
                    b = b.expandRow(m.rows)
                    m.multiplyToParallel(a, b)
                }
                storedInA = !storedInA
            }
            val result = if (storedInA) b else a
            result.shrinkRow(this.rows)*/
        }
    }
}