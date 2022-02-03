package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.PrimeWithGenerator
import cryptography.lattice.ring_lwe.ring.RootDataUIntPrime
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import math.abstract_structure.Ring
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
@Deprecated("aaa")
class DiscreteFourierTransformMatrixPrimeDeprecated<A>(val root: RootDataUIntPrime<A>) : AbstractSquareMatrix<A> {

    override val ring: Ring<A> get() = root.ring

    override val size: UInt get() = root.order.value

    override fun elementAtUnsafe(row: UInt, column: UInt): A = ring.powerM(root.root, modTimes(row, column, root.order.value))

    val primeWithGenerator: PrimeWithGenerator = runBlocking { PrimeWithGenerator(root.order.value, firstMultiplicativeGeneratorOfPrimeFieldUnsafe(root.order.value)) }

    val rootPower: List<A> = ring.powers(root.root, 0u until root.order.value)
    val rgInv: List<A> = list(root.order.value - 1u) { i -> rootPower[primeWithGenerator.inverseGeneratorPower(i).toInt()] }

    override fun determinant(): A {
        TODO()
    }

    override fun hasInverse(): Boolean {
        TODO()
    }

    override fun inverse(): AbstractSquareMatrix<A> {
        TODO()
    }

    fun timesImpl(vector: AbstractColumnVector<A>): ColumnVector<A> {
        val result = MutableList(vector.size.toInt()) { ring.zero }
        var sum = ring.zero
        for (i in 0u until vector.size) {
            sum = ring.add(sum, vector.vectorElementAtUnsafe(i))
        }
        result[0] = sum

        val xg = list(primeWithGenerator.primeDec) { i -> vector.vectorElementAtUnsafe(primeWithGenerator.generatorPower(i)) }
        val convolution = ring.convolution(rgInv, xg)

        val v0 = vector.vectorElementAtUnsafe(0u)

        for (i in convolution.indices) {
            result[primeWithGenerator.inverseGeneratorPower(i.toUInt()).toInt()] = ring.add(v0, convolution[i])
        }
        return ColumnVector(ring, result)
    }

    fun multiplyToImpl(vector: AbstractColumnVector<A>, dest: AbstractMutableMatrix<A>) {
        var sum = ring.zero
        for (i in 0u until vector.size) {
            sum = ring.add(sum, vector.vectorElementAtUnsafe(i))
        }
        dest.setElementAtUnsafe(0u, 0u, sum)

        val xg = list(primeWithGenerator.primeDec) { i -> vector.vectorElementAtUnsafe(primeWithGenerator.generatorPower(i)) }

        val convolution = ring.convolution(rgInv, xg)
        val v0 = vector.vectorElementAtUnsafe(0u)
        for (i in convolution.indices) {
            dest.setElementAtUnsafe(primeWithGenerator.inverseGeneratorPower(i.toUInt()), 0u, ring.add(v0, convolution[i]))
        }
    }

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = if (root.order.value <= 3uL) super.timesImpl(matrix) else
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
                    for (i in 0u until columnVector.size) {
                        data[i.toInt()] += columnVector.vectorElementAtUnsafe(i)
                    }
                }
                OrdinaryMatrix(ring, this.rows, matrix.columns, data)
            }
        }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = if (root.order.value <= 3uL) super.timesRowParallelImpl(matrix) else
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
                    val result = ring.zeroMutableMatrix(this@DiscreteFourierTransformMatrixPrimeDeprecated.rows, matrix.columns)
                    for ((i, vector) in matrix.columnVectorViews().withIndex()) {
                        launch {
                            this@DiscreteFourierTransformMatrixPrimeDeprecated.multiplyToImpl(vector, result.mutableColumnVectorViewAt(i.toUInt()))
                        }
                    }
                    result
                }
            }
        }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        if (root.order.value <= 3uL) super.multiplyToImpl(matrix, dest)
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
        if (root.order.value <= 3uL) super.multiplyToRowParallelImpl(matrix, dest)
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
                            this@DiscreteFourierTransformMatrixPrimeDeprecated.multiplyToImpl(vector, dest.mutableColumnVectorViewAt(i.toUInt()))
                        }
                    }
                }
            }
        }
    }
}