package math.martix.tensor

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.CRing
import math.abstract_structure.instance.ringUInt
import math.diagonalBlockSizeEquals
import math.martix.AbstractMatrix
import math.martix.Constant
import math.martix.mutable.MutableMatrix
import math.operations.innerProduct
import math.operations.product
import math.runningFoldRight

/**
 * Created by CowardlyLion at 2022/1/8 20:27
 */
class FormalSquareTensorProduct<A>(ring: CRing<A>, val diagonalBlock: List<AbstractMatrix<A>>) : AbstractMatrix<A>(ring, diagonalBlock.fold(1u) { acc, m -> acc * m.rows }, diagonalBlock.fold(1u) { acc, m -> acc * m.rows }) {

    init {
        for (matrix in diagonalBlock) {
            require(matrix.isSquareMatrix)
        }
    }

    //    lack a method of runningFoldRight
    val indexBase = diagonalBlock.runningFoldRight(1u) { acc, m -> acc * m.rows }.drop(1)

    private fun decodeIndices(index: UInt): MutableList<UInt> {
        val indices = mutableListOf<UInt>()
        var n = index
        for (base in indexBase) {
            indices += n / base
            n %= base
        }
        return indices
    }

    fun encodeIndex(indices: List<UInt>): UInt = ringUInt.innerProduct(indexBase, indices)

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        val rowIndices = decodeIndices(row)
        val columnIndices = decodeIndices(column)
        return ring.product(List(diagonalBlock.size) { i -> diagonalBlock[i].elementAt(rowIndices[i], columnIndices[i]) })
    }

    private fun singleWhiskeredMultiplyTo(blockIndex: UInt, matrix: AbstractMatrix<A>, dest: MutableMatrix<A>): MutableMatrix<A> {
        val r = indexBase[blockIndex.toInt()]
        val block = diagonalBlock[blockIndex.toInt()]
        val n = block.rows
        val l = rows / (r * n)
        for (il in 0u until l) {
            for (ir in 0u until r) {
                val base = il * n * r + ir
                val submatrix = matrix.sparseSubmatrixView(base, r, n)
                val destSubmatrix = dest.mutableSparseSubmatrixView(base, r, n)
                block.multiplyTo(submatrix, destSubmatrix)
            }
        }
        return dest
    }

    private suspend fun singleWhiskeredMultiplyToParallel(blockIndex: UInt, matrix: AbstractMatrix<A>, dest: MutableMatrix<A>): MutableMatrix<A> = coroutineScope {
        val r = indexBase[blockIndex.toInt()]
        val block = diagonalBlock[blockIndex.toInt()]
        val n = block.rows
        val l = rows / (r * n)
        for (il in 0u until l) {
            for (ir in 0u until r) {
                val base = il * n * r + ir
                val submatrix = matrix.sparseSubmatrixView(base, r, n)
                val destSubmatrix = dest.mutableSparseSubmatrixView(base, r, n)
                launch {
                    block.multiplyToParallel(submatrix, destSubmatrix)
                }
            }
        }
        dest
    }

//    No need to implement a modify-version of multiplication, just need to switch between two MutableMatrix.
//    private fun singleWhiskeredMultiplyTo(blockIndex: UInt, matrix: MutableMatrix<A>): Matrix<A> {


    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when {
        diagonalBlock.isEmpty()                                                      -> super.timesImpl(matrix)   //empty tensor product is 1*1 matrix (id_A)
        diagonalBlock.size == 1                                                      -> diagonalBlock[0].times(matrix)
        matrix is FormalSquareTensorProduct<A>
                && diagonalBlockSizeEquals(this.diagonalBlock, matrix.diagonalBlock) -> {   //It's possible to implement a more intelligent mix-tensor-product resolution method.
            FormalSquareTensorProduct(ring, diagonalBlock.zip(matrix.diagonalBlock).map { (a, b) -> a * b })
        }
        else                                                                         -> {
            var blockIndex = 0u
            val a = singleWhiskeredMultiplyTo(blockIndex++, matrix, matrix.toMutableMatrix())
            val b = singleWhiskeredMultiplyTo(blockIndex++, a, matrix.toMutableMatrix())
            var first = true
            while (blockIndex < diagonalBlock.size.toUInt()) {
                if (first) {
                    singleWhiskeredMultiplyTo(blockIndex, b, a)
                } else {
                    singleWhiskeredMultiplyTo(blockIndex, a, b)
                }
                first = !first
                blockIndex++
            }
            if (first) b else a
        }
    }

    override fun downCast(): AbstractMatrix<A> = when {
        diagonalBlock.isEmpty() -> Constant(ring, ring.one)
        diagonalBlock.size == 1 -> diagonalBlock[0]
        else                    -> this
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when {
        diagonalBlock.isEmpty()                                                      -> super.timesRowParallelImpl(matrix)   //empty tensor product is 1*1 matrix (id_A)
        diagonalBlock.size == 1                                                      -> diagonalBlock[0].times(matrix)
        matrix is FormalSquareTensorProduct<A>
                && diagonalBlockSizeEquals(this.diagonalBlock, matrix.diagonalBlock) -> {   //It's possible to implement a more intelligent mix-tensor-product resolution method.
            FormalSquareTensorProduct(ring, diagonalBlock.zip(matrix.diagonalBlock).map { (a, b) -> a.timesRowParallel(b) })
        }
        else                                                                         -> {
            var blockIndex = 0u
            val a = singleWhiskeredMultiplyToParallel(blockIndex++, matrix, matrix.toMutableMatrix())
            val b = singleWhiskeredMultiplyToParallel(blockIndex++, a, matrix.toMutableMatrix())
            var first = true
            while (blockIndex < diagonalBlock.size.toUInt()) {
                if (first) {
                    singleWhiskeredMultiplyToParallel(blockIndex, b, a)
                } else {
                    singleWhiskeredMultiplyToParallel(blockIndex, a, b)
                }
                first = !first
                blockIndex++
            }
            if (first) b else a
        }
    }
}