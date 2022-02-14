package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.coding.LadderSwitcher
import cryptography.lattice.ring_lwe.matrix.ReducedTwiddleMatrix
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPI
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import math.integer.uint.modTimes
import math.martix.*

/**
 * Created by CowardlyLion at 2022/2/13 22:08
 */
class ProperPrimePowerCeMatrix<A>(override val root: RootUIntPP<A>, val primeCase: CeMatrixPI<A>, val decreaseCase: DftMatrixPPI<A>) : CeMatrixPPI<A>, AbstractSquareFormalProduct<A> {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = root.cachedPower(modTimes(root.order.coprimeNumberAtUnsafe(row), column, root.order.value))

    private val primeDec: UInt get() = primeCase.size
    private val decPower: UInt get() = decreaseCase.size    //decreaseCase is DFT

    override val matrices: List<AbstractSquareMatrix<A>> = listOf(
        ring.permutationMatrix(LadderSwitcher(size, primeDec, decPower)),
        ring.whiskered(size, primeDec, decreaseCase, 1u),
        ReducedTwiddleMatrix(ring, size, primeDec, decPower, root),
        ring.whiskered(size, 1u, primeCase, decPower)
    )

    override fun hasInverse(): Boolean {
        TODO("Not yet implemented")
    }

    override val inverse: AbstractSquareMatrix<A> by lazy {
        SquareFormalProduct(
            ring, size, listOf(
                ring.whiskered(size, 1u, primeCase.inverse, decPower),
                ReducedTwiddleMatrix(ring, size, primeDec, decPower, root.inverse),
                ring.whiskered(size, primeDec, decreaseCase.inverse, 1u),
                ring.permutationMatrix(LadderSwitcher(size, decPower, primeDec)),
            )
        )
    }

    override fun determinant(): A {
        TODO("Not yet implemented")
    }

}