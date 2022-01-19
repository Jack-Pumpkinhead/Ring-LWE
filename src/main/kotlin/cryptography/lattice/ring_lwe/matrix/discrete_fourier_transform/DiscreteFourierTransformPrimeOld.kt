package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.Ring
import math.integer.operation.modTimes
import util.stdlib.list
import math.martix.*
import math.martix.concrete.ColumnVector
import math.martix.concrete.Constant
import math.martix.concrete.OrdinaryMatrix
import math.martix.mutable.AbstractMutableMatrix
import math.vector.convolution

/**
 * Created by CowardlyLion at 2022/1/16 15:04,
 * [rootPower] is (0 to p-1)-th powers of a primitive p-th root of unity of [ring]
 */
@Deprecated("to removed")
class DiscreteFourierTransformPrimeOld<A>(ring: Ring<A>, val primeData: PrimeData, val rootPower: List<A>) : AbstractMatrix<A>(ring, primeData.prime, primeData.prime) {


    val rgInv: List<A> = list(primeData.primeDec) { i -> rootPower[primeData.inverseGeneratorPower(i).toInt()] }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        return rootPower[modTimes(row, column, primeData.prime).toInt()]
    }

    fun timesImpl(vector: AbstractColumnVector<A>): ColumnVector<A> {
        val result = MutableList(vector.vectorSize.toInt()) { ring.zero }
        var sum = ring.zero
        for (i in 0u until vector.vectorSize) {
            sum = ring.add(sum, vector.vectorElementAtUnsafe(i))
        }
        result[0] = sum

        val xg = list(primeData.primeDec) { i -> vector.vectorElementAtUnsafe(primeData.generatorPower(i)) }

        val convolution = ring.convolution(rgInv, xg)
        for (i in convolution.indices) {
            result[primeData.inverseGeneratorPower(i.toUInt()).toInt()] = convolution[i]
        }
        return ColumnVector(ring, result)
    }

    fun multiplyToImpl(vector: AbstractColumnVector<A>, dest: AbstractMutableMatrix<A>) {
        var sum = ring.zero
        for (i in 0u until vector.vectorSize) {
            sum = ring.add(sum, vector.vectorElementAtUnsafe(i))
        }
        dest.setElementAtUnsafe(0u, 0u, sum)

        val xg = list(primeData.primeDec) { i -> vector.vectorElementAtUnsafe(primeData.generatorPower(i)) }

        val convolution = ring.convolution(rgInv, xg)
        for (i in convolution.indices) {
            dest.setElementAtUnsafe(i.toUInt() + 1u, 0u, convolution[i])
        }
    }

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is Constant<A>               -> matrix
        is AbstractRowVector<A>      -> matrix
        is AbstractColumnVector<A>   -> timesImpl(matrix)
        is IdentityMatrix<A>         -> super.timesImpl(matrix)
        is ZeroMatrix<A>             -> super.timesImpl(matrix)
        is PermutationMatrix<A>      -> super.timesImpl(matrix)
        is AbstractDiagonalMatrix<A> -> super.timesImpl(matrix)
        else                         -> {
            val data = List(this.rows.toInt()) { mutableListOf<A>() }
            for (vector in matrix.columnVectorViews()) {
                val columnVector = this.timesImpl(vector)
                for (i in 0u until columnVector.vectorSize) {
                    data[i.toInt()] += columnVector.vectorElementAtUnsafe(i)
                }
            }
            OrdinaryMatrix(ring, data)
        }
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is Constant<A>               -> matrix
        is AbstractRowVector<A>      -> matrix
        is AbstractColumnVector<A>   -> timesImpl(matrix)
        is IdentityMatrix<A>         -> super.timesImpl(matrix)
        is ZeroMatrix<A>             -> super.timesImpl(matrix)
        is PermutationMatrix<A>      -> super.timesImpl(matrix)
        is AbstractDiagonalMatrix<A> -> super.timesImpl(matrix)
        else                         -> {
            coroutineScope {
                val result = ring.zeroMutableMatrix(this@DiscreteFourierTransformPrimeOld.rows, matrix.columns)
                for ((i, vector) in matrix.columnVectorViews().withIndex()) {
                    launch {
                        this@DiscreteFourierTransformPrimeOld.multiplyToImpl(vector, result.mutableColumnVectorViewAt(i.toUInt()))
                    }
                }
                result
            }
        }
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>               -> dest.setUnsafe(matrix)
            is AbstractRowVector<A>      -> dest.setUnsafe(matrix)
            is AbstractColumnVector<A>   -> multiplyToImpl(matrix, dest)
            is IdentityMatrix<A>         -> super.multiplyToImpl(matrix, dest)
            is ZeroMatrix<A>             -> super.multiplyToImpl(matrix, dest)
            is PermutationMatrix<A>      -> super.multiplyToImpl(matrix, dest)
            is AbstractDiagonalMatrix<A> -> super.multiplyToImpl(matrix, dest)
            else                         -> {
                for ((i, vector) in matrix.columnVectorViews().withIndex()) {
                    multiplyToImpl(vector, dest.mutableColumnVectorViewAt(i.toUInt()))
                }
            }
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>               -> dest.setUnsafeRowParallel(matrix)
            is AbstractRowVector<A>      -> dest.setUnsafeRowParallel(matrix)
            is AbstractColumnVector<A>   -> multiplyToImpl(matrix, dest)
            is IdentityMatrix<A>         -> super.multiplyToRowParallelImpl(matrix, dest)
            is ZeroMatrix<A>             -> super.multiplyToRowParallelImpl(matrix, dest)
            is PermutationMatrix<A>      -> super.multiplyToRowParallelImpl(matrix, dest)
            is AbstractDiagonalMatrix<A> -> super.multiplyToRowParallelImpl(matrix, dest)
            else                         -> {
                coroutineScope {
                    for ((i, vector) in matrix.columnVectorViews().withIndex()) {
                        launch {
                            this@DiscreteFourierTransformPrimeOld.multiplyToImpl(vector, dest.mutableColumnVectorViewAt(i.toUInt()))
                        }
                    }
                }
            }
        }
    }
}