package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.coding.permCLInv
import cryptography.lattice.ring_lwe.coding.permLRInv
import cryptography.lattice.ring_lwe.ring.RootDataUInt
import math.abstract_structure.Ring
import math.integer.operation.modTimes
import math.martix.*
import math.martix.tensor.SquareFormalKroneckerProduct
import math.martix.tensor.SquareFormalProduct

/**
 * Created by CowardlyLion at 2022/1/19 17:44
 */
open class DftMatrix<A>(val root: RootDataUInt<A>) : BackingSquareMatrix<A> {

    final override val ring: Ring<A> get() = root.ring

    override fun elementAtUnsafe(row: UInt, column: UInt): A = root.power(modTimes(row, column, root.order.value))

    override val underlyingMatrix: AbstractSquareMatrix<A> =
        if (root.order.factors.size == 1) {
            val root1 = root.toPrimePower()
            if (root1.order.power == 1u) {
                DftMatrixPrime(root1.toPrime())
            } else {
                DftMatrixPrimePower(root1)
            }
        } else {
            val factors = root.order.factors.map { it.value }
            SquareFormalProduct(
                ring, listOf(
                    ring.permutationMatrix(permCLInv(factors)),
                    SquareFormalKroneckerProduct(ring, root.allMaximalPrimePowerSubroot().map { root1 ->
                        if (root1.order.power == 1u) {
                            DftMatrixPrime(root1.toPrime())
                        } else {
                            DftMatrixPrimePower(root1)
                        }
                    }),
                    ring.permutationMatrix(permLRInv(factors))
                )
            )
        }

    override fun hasInverse(): Boolean = ring.hasInverse(ring.ofInteger(size))

    override fun inverse(): AbstractSquareMatrix<A> {   //TODO inverse of size in Z/(p) equals (p-1)^2/size mod p, maybe faster than gcd algorithm, but this is only a O(1) improvement
        return ring.formalProduct(DftMatrix(root.conjugate()), ring.scalarMatrix(size, ring.inverse(ring.ofInteger(size))))
    }
}