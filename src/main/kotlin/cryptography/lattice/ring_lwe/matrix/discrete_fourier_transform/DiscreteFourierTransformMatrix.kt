package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.coding.permCLInv
import cryptography.lattice.ring_lwe.coding.permLRInv
import cryptography.lattice.ring_lwe.ring.RootDataUInt
import math.abstract_structure.Ring
import math.integer.operation.modTimes
import math.martix.AbstractSquareMatrix
import math.martix.BackingSquareMatrix
import math.martix.permutationMatrix
import math.martix.tensor.SquareFormalKroneckerProduct
import math.martix.tensor.SquareFormalProduct

/**
 * Created by CowardlyLion at 2022/1/19 17:44
 */
class DiscreteFourierTransformMatrix<A>(val root: RootDataUInt<A>) : BackingSquareMatrix<A> {

    override val ring: Ring<A> get() = root.ring

    override fun elementAtUnsafe(row: UInt, column: UInt): A = root.power(modTimes(row, column, root.order.value))

    override val underlyingMatrix: AbstractSquareMatrix<A> =
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
}