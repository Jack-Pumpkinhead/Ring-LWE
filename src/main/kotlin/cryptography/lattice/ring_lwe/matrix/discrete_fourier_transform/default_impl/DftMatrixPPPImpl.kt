package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.default_impl

import cryptography.lattice.ring_lwe.coding.permCLInvMatrix
import cryptography.lattice.ring_lwe.coding.permLCInvMatrix
import cryptography.lattice.ring_lwe.coding.permLRInvMatrix
import cryptography.lattice.ring_lwe.coding.permRLInvMatrix
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPI
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import math.martix.AbstractSquareFormalProduct
import math.martix.AbstractSquareMatrix
import math.martix.SquareFormalProduct
import math.martix.tensor.SquareFormalKroneckerProduct

/**
 * Created by CowardlyLion at 2022/2/13 15:55
 */
class DftMatrixPPPImpl<A>(override val root: RootPPP<A>, val subDftMatrices: List<DftMatrixPPI<A>>) : DftMatrixPPP<A>, AbstractSquareFormalProduct<A> {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = super<DftMatrixPPP>.elementAtUnsafe(row, column)

    private val factors = root.order.factors.map { it.value }

    override val matrices: List<AbstractSquareMatrix<A>> = listOf(
        ring.permCLInvMatrix(size, factors),
        SquareFormalKroneckerProduct(ring, size, subDftMatrices),
        ring.permLRInvMatrix(size, factors)
    )

    override fun hasInverse(): Boolean = ring.hasInverse(ring.ofInteger(size))

    override val inverse: AbstractSquareMatrix<A> by lazy {
        SquareFormalProduct(
            ring, size, listOf(
                ring.permRLInvMatrix(size, factors),
                SquareFormalKroneckerProduct(ring, size, subDftMatrices.map { it.inverse }),
                ring.permLCInvMatrix(size, factors)
            )
        )
    }


}