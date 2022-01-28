package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.coding.permCLInv
import cryptography.lattice.ring_lwe.coding.permLRInv
import cryptography.lattice.ring_lwe.matrix.RootDataUInt
import math.integer.operation.modTimes
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.mutable.AbstractMutableMatrix
import math.martix.permutationMatrix
import math.martix.tensor.SquareFormalKroneckerProduct
import math.martix.tensor.SquareFormalProduct
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/19 17:44
 */
class DiscreteFourierTransformMatrix<A>(val root: RootDataUInt<A>) : AbstractSquareMatrix<A>(root.ring, root.order.value) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = ring.powerM(root.root, modTimes(row, column, root.order.value))

    val underlyingMatrix: AbstractSquareMatrix<A> =
        if (root.order.factors.size == 1) {
            val root1 = root.toPrimePower()
            if (root1.order.power == 1u) {
                DiscreteFourierTransformMatrixPrime(root1.toPrime())
            } else {
                val primeCase = DiscreteFourierTransformMatrixPrime(root1.primeSubroot())
                DiscreteFourierTransformMatrixPrimePower(root1, primeCase)
            }
        } else {
            val factors = root.order.factors.map { it.value }
            SquareFormalProduct(
                ring, listOf(
                    ring.permutationMatrix(permCLInv(factors)),
                    SquareFormalKroneckerProduct(ring, root.allMaximalPrimePowerSubroot().map { root1 ->
                        if (root1.order.power == 1u) {
                            DiscreteFourierTransformMatrixPrime(root1.toPrime())
                        } else {
                            DiscreteFourierTransformMatrixPrimePower(root1, DiscreteFourierTransformMatrixPrime(root1.primeSubroot()))
                        }
                    }),
                    ring.permutationMatrix(permLRInv(factors))
                )
            )
        }

    override fun determinant(): A = underlyingMatrix.determinant()

    override fun hasInverse(): Boolean = underlyingMatrix.hasInverse()

    override fun inverse(): AbstractSquareMatrix<A> = underlyingMatrix.inverse()

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = underlyingMatrix.timesImpl(matrix)

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = underlyingMatrix.timesRowParallelImpl(matrix)

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        underlyingMatrix.multiplyToImpl(matrix, dest)
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        underlyingMatrix.multiplyToRowParallelImpl(matrix, dest)
    }
}