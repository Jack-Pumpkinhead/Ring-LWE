package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.coding.permCLInvMatrix
import cryptography.lattice.ring_lwe.coding.permLCInvMatrix
import cryptography.lattice.ring_lwe.coding.permLRInvMatrix
import cryptography.lattice.ring_lwe.coding.permRLInvMatrix
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import math.martix.AbstractSquareFormalProduct
import math.martix.AbstractSquareMatrix
import math.martix.SquareFormalProduct
import math.martix.tensor.SquareFormalKroneckerProduct

/**
 * Created by CowardlyLion at 2022/2/13 15:55
 */
class ProperPrimePowerProductDftMatrix<A>(val builder: DftMatrixPPIBuilder<A>, override val root: RootUIntPPP<A>) : DftMatrixPPPI<A>, AbstractSquareFormalProduct<A> {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = super<DftMatrixPPPI>.elementAtUnsafe(row, column)

    private val factors = root.order.factors.map { it.value }

    private val matrix1 = SquareFormalKroneckerProduct(ring, size, root.allMaximalPrimePowerSubroot().map { builder.build(it) })

    override val matrices: List<AbstractSquareMatrix<A>> = listOf(ring.permCLInvMatrix(size, factors), matrix1, ring.permLRInvMatrix(size, factors))

    override fun hasInverse(): Boolean = ring.hasInverse(ring.ofInteger(size))

    override val inverse: AbstractSquareMatrix<A> by lazy {
        SquareFormalProduct(ring, size, listOf(ring.permRLInvMatrix(size, factors), matrix1.inverse, ring.permLCInvMatrix(size, factors)))
    }


}