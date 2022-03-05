package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixBuilder
import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.matrix
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/2/27 20:35
 */
class CeInverseMatrixP<A>(override val inverse: CeMatrixP<A>, dftBuilder: DftMatrixBuilder<A>) : AbstractSquareMatrix<A> {

    override val ring: Ring<A> get() = inverse.ring

    override val size: UInt get() = inverse.size

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        return ring.multiply(inverseP, ring.subtract(inverse.root.root.cachedInversePower(row * (column + 1u)), inverse.root.root.cachedPower(column + 1u)))
    }

    override fun determinant(): A {
        TODO("Not yet implemented")
    }

    val rowRootPower = RowVectorPrimeRootPower(inverse.root)
    val conjugateDft = dftBuilder.build(inverse.root.inverse)
    val inverseP = ring.inverse(ring.ofInteger(inverse.root.order.value))

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        val row = rowRootPower * matrix
        val a = conjugateDft * MatrixPaddingZeroFirstRow(matrix)
        return ring.matrix(this.rows, matrix.columns) { i, j ->
            ring.multiply(inverseP, ring.subtract(a.elementAtUnsafe(i, j), row.elementAtUnsafe(0u, j)))
        }
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        val row = rowRootPower.timesRowParallelImpl(matrix)
        val a = conjugateDft.timesRowParallelImpl(MatrixPaddingZeroFirstRow(matrix))
        return ring.matrix(this.rows, matrix.columns) { i, j ->
            ring.multiply(inverseP, ring.subtract(a.elementAtUnsafe(i, j), row.elementAtUnsafe(0u, j)))
        }
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        val row = rowRootPower * matrix
        val a = conjugateDft * MatrixPaddingZeroFirstRow(matrix)
        dest.set { i, j ->
            ring.multiply(inverseP, ring.subtract(a.elementAtUnsafe(i, j), row.elementAtUnsafe(0u, j)))
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        val row = rowRootPower.timesRowParallelImpl(matrix)
        val a = conjugateDft.timesRowParallelImpl(MatrixPaddingZeroFirstRow(matrix))
        dest.set { i, j ->
            ring.multiply(inverseP, ring.subtract(a.elementAtUnsafe(i, j), row.elementAtUnsafe(0u, j)))
        }
    }
}