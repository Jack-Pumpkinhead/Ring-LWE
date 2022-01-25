package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import math.integer.firstMultiplicativeGeneratorOfPrimeFieldUnsafe
import math.integer.operation.modTimes
import math.martix.*
import math.martix.concrete.ColumnVector
import math.martix.concrete.Constant
import math.martix.concrete.OrdinaryMatrix
import math.martix.mutable.AbstractMutableMatrix
import math.operation.powers
import math.powerM
import math.vector.convolution
import util.stdlib.list

/**
 * Created by CowardlyLion at 2022/1/19 18:26
 */
class DiscreteFourierTransformMatrixPrime<A>(val root: RootData<A>) : AbstractMatrix<A>(root.ring, root.order.toUInt(), root.order.toUInt()) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = ring.powerM(root.root, modTimes(row, column, root.order.toUInt()))

    val primeData: PrimeData

    val rootPower: List<A>
    val rgInv: List<A>

    init {
        require(root.orderFactorization.size == 1)
        require(root.orderFactorization[0].power == 1u)
        val prime = root.orderFactorization[0].prime

        primeData = runBlocking { PrimeData(prime.toUInt(), firstMultiplicativeGeneratorOfPrimeFieldUnsafe(prime).toUInt()) }
        rootPower = ring.powers(root.root, 0u until root.order.toUInt())
        rgInv = list(prime.toUInt() - 1u) { i -> rootPower[primeData.inverseGeneratorPower(i).toInt()] }
    }


    fun timesImpl(vector: AbstractColumnVector<A>): ColumnVector<A> {
        val result = MutableList(vector.vectorSize.toInt()) { ring.zero }
        var sum = ring.zero
        for (i in 0u until vector.vectorSize) {
            sum = ring.add(sum, vector.vectorElementAtUnsafe(i))
        }
        result[0] = sum

        val xg = list(primeData.primeDec) { i -> vector.vectorElementAtUnsafe(primeData.generatorPower(i)) }

//        println("rgInv: $rgInv")
//        println("gPower: ${primeData.generatorPower}")
//        println("xg: $xg")
        val convolution = ring.convolution(rgInv, xg)
//        println("convolution: $convolution")
//        println()
        val v0 = vector.vectorElementAtUnsafe(0u)
        for (i in convolution.indices) {
            result[primeData.inverseGeneratorPower(i.toUInt()).toInt()] = ring.add(v0, convolution[i])
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
        val v0 = vector.vectorElementAtUnsafe(0u)
        for (i in convolution.indices) {
            dest.setElementAtUnsafe(primeData.inverseGeneratorPower(i.toUInt()), 0u, ring.add(v0, convolution[i]))
        }
    }

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = if (root.order <= 3uL) super.timesImpl(matrix) else
        when (matrix) {
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

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = if (root.order <= 3uL) super.timesRowParallelImpl(matrix) else
        when (matrix) {
            is Constant<A>               -> matrix
            is AbstractRowVector<A>      -> matrix
            is AbstractColumnVector<A>   -> timesImpl(matrix)
            is IdentityMatrix<A>         -> super.timesImpl(matrix)
            is ZeroMatrix<A>             -> super.timesImpl(matrix)
            is PermutationMatrix<A>      -> super.timesImpl(matrix)
            is AbstractDiagonalMatrix<A> -> super.timesImpl(matrix)
            else                         -> {
                coroutineScope {
                    val result = ring.zeroMutableMatrix(this@DiscreteFourierTransformMatrixPrime.rows, matrix.columns)
                    for ((i, vector) in matrix.columnVectorViews().withIndex()) {
                        launch {
                            this@DiscreteFourierTransformMatrixPrime.multiplyToImpl(vector, result.mutableColumnVectorViewAt(i.toUInt()))
                        }
                    }
                    result
                }
            }
        }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        if (root.order <= 3uL) super.multiplyToImpl(matrix, dest)
        else when (matrix) {
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
        if (root.order <= 3uL) super.multiplyToRowParallelImpl(matrix, dest)
        else when (matrix) {
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
                            this@DiscreteFourierTransformMatrixPrime.multiplyToImpl(vector, dest.mutableColumnVectorViewAt(i.toUInt()))
                        }
                    }
                }
            }
        }
    }
}