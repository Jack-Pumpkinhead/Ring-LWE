package cryptography.lattice.ring_lwe.matrix.canonical_embedding.conjugate_transpose

import cryptography.lattice.ring_lwe.coding.LadderSwitcher
import cryptography.lattice.ring_lwe.matrix.ReducedTwiddleMatrix
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPI
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import math.martix.*

/**
 * Created by CowardlyLion at 2022/2/24 19:18
 */
class ProperPrimePowerCeMatrixCT<A>(override val root: RootUIntPP<A>, val primeCase: CeMatrixPICT<A>, val decreaseCase: DftMatrixPPI<A>) : CeMatrixPPICT<A>, AbstractSquareFormalProduct<A> {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = super<CeMatrixPPICT>.elementAtUnsafe(row, column)

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