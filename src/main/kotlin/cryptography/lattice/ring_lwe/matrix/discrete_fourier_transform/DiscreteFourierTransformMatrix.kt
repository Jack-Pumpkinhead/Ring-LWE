package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.coding.permCLInv
import cryptography.lattice.ring_lwe.coding.permLRInv
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
class DiscreteFourierTransformMatrix<A>(val root: RootDataULong<A>) : AbstractSquareMatrix<A>(root.ring, root.order.toUInt()) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = ring.powerM(root.root, modTimes(row, column, root.order.toUInt()))

    val underlyingMatrix: AbstractMatrix<A> =
        if (root.orderFactorization.size == 1) {
            val factor = root.orderFactorization[0]
            if (factor.power == 1u) {
                DiscreteFourierTransformMatrixPrime(root)
            } else {
                val primeCase = DiscreteFourierTransformMatrixPrime(root.subRootData(listOf(factor.power - 1u)))
                DiscreteFourierTransformMatrixPrimePower(root, primeCase)
            }
        } else {
            val factors = root.orderFactorization.map { it.primePower.toUInt() }
            FormalProduct(
                ring, listOf(
                    ring.permutationMatrix(permCLInv(factors)),
                    FormalKroneckerProduct(ring, root.allSubRootDataPrimePower().map { root1 ->
                        val power1 = root1.orderFactorization[0].power
                        if (power1 == 1u) {
                            DiscreteFourierTransformMatrixPrime(root1)
                        } else {
                            val primeCase = DiscreteFourierTransformMatrixPrime(root1.subRootData(listOf(power1 - 1u)))
                            DiscreteFourierTransformMatrixPrimePower(root1, primeCase)
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