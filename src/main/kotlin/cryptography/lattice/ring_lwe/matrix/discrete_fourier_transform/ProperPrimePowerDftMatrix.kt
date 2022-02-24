package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.coding.LadderSwitcher
import cryptography.lattice.ring_lwe.matrix.TwiddleMatrix
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import math.martix.*

/**
 * Created by CowardlyLion at 2022/2/13 13:42
 *
 * [decreaseCase] is DFT matrix with power decreased by 1
 */
class ProperPrimePowerDftMatrix<A>(override val root: RootUIntPP<A>, val primeCase: DftMatrixPI<A>, val decreaseCase: DftMatrixPPI<A>) : DftMatrixPPI<A>, AbstractSquareFormalProduct<A> {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = super<DftMatrixPPI>.elementAtUnsafe(row, column)

    private val prime: UInt get() = primeCase.size
    private val decOrder: UInt get() = decreaseCase.size

    override val matrices: List<AbstractSquareMatrix<A>> = listOf(
        ring.permutationMatrix(LadderSwitcher(size, prime, decOrder)),
        ring.whiskered(size, prime, decreaseCase, 1u),
        TwiddleMatrix(ring, size, prime, decOrder, root),
        ring.whiskered(size, 1u, primeCase, decOrder)
    )

    override fun hasInverse(): Boolean = ring.hasInverse(ring.ofInteger(size))

    //TODO compare speed and accuracy of different version of inverse
    override val inverse: AbstractSquareMatrix<A> by lazy {
        SquareFormalProduct(
            ring, size, listOf(
                ring.whiskered(size, 1u, primeCase.inverse, decOrder),
                TwiddleMatrix(ring, size, prime, decOrder, root.inverse),
                ring.whiskered(size, prime, decreaseCase.inverse, 1u),
                ring.permutationMatrix(LadderSwitcher(size, decOrder, prime))
            )
        )
    }


}