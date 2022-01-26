package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.coding.permCLInv
import cryptography.lattice.ring_lwe.coding.permLRInv
import cryptography.lattice.ring_lwe.matrix.RootDataUInt
import math.integer.operation.modTimes
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.FormalProduct
import math.martix.mutable.AbstractMutableMatrix
import math.martix.permutationMatrix
import math.martix.tensor.FormalKroneckerProduct
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/19 17:44
 */
class DiscreteFourierTransformMatrix<A>(val root: RootDataUInt<A>) : AbstractSquareMatrix<A>(root.ring, root.order) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = ring.powerM(root.root, modTimes(row, column, root.order))

    val underlyingMatrix: AbstractMatrix<A> =
        if (root.orderFactorization.size == 1) {
            val root1 = root.toPrimePower()
            if (root1.order.power == 1u) {
                DiscreteFourierTransformMatrixPrime(root1.toPrime())
            } else {
                val primeCase = DiscreteFourierTransformMatrixPrime(root1.primeSubroot())
                DiscreteFourierTransformMatrixPrimePower(root1, primeCase)
            }
        } else {
            val factors = root.orderFactorization.map { it.primePower }
            FormalProduct(
                ring, listOf(
                    ring.permutationMatrix(permCLInv(factors)),
                    FormalKroneckerProduct(ring, root.allPrimePowerSubroot().map { root1 ->
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

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = underlyingMatrix.timesImpl(matrix)

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = underlyingMatrix.timesRowParallelImpl(matrix)

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        underlyingMatrix.multiplyToImpl(matrix, dest)
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        underlyingMatrix.multiplyToRowParallelImpl(matrix, dest)
    }
}