package cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.default_impl

import cryptography.lattice.ring_lwe.coding.LadderSwitcher
import cryptography.lattice.ring_lwe.matrix.ReducedTwiddleMatrix
import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.CeCTMatrixP
import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.CeCTMatrixPP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPI
import cryptography.lattice.ring_lwe.ring.RootPP
import math.martix.*

/**
 * Created by CowardlyLion at 2022/2/24 19:18
 */
class CeCTMatrixPPImpl<A>(override val root: RootPP<A>, val primeCase: CeCTMatrixP<A>, val decreaseCase: DftMatrixPPI<A>) : CeCTMatrixPP<A>, AbstractSquareFormalProduct<A> {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = super<CeCTMatrixPP>.elementAtUnsafe(row, column)

    private val primeDec: UInt get() = primeCase.size
    private val decPower: UInt get() = decreaseCase.size    //decreaseCase is conjugate DFT

    override val matrices: List<AbstractSquareMatrix<A>> = listOf(
        ring.whiskered(size, 1u, primeCase, decPower),
        ReducedTwiddleMatrix(ring, size, primeDec, decPower, root.inverse),
        ring.whiskered(size, primeDec, decreaseCase, 1u),
        ring.permutationMatrix(LadderSwitcher(size, decPower, primeDec)),
    )

    override fun hasInverse(): Boolean {
        TODO("Not yet implemented")
    }

    override val inverse: AbstractSquareMatrix<A> by lazy {
        SquareFormalProduct(
            ring, size, listOf(
                ring.permutationMatrix(LadderSwitcher(size, primeDec, decPower)),
                ring.whiskered(size, primeDec, decreaseCase.inverse, 1u),
                ReducedTwiddleMatrix(ring, size, primeDec, decPower, root),
                ring.whiskered(size, 1u, primeCase.inverse, decPower),
            )
        )
    }

    override fun determinant(): A {
        TODO("Not yet implemented")
    }


}