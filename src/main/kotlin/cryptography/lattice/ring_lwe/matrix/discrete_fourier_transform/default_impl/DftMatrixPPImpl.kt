package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.default_impl

import cryptography.lattice.ring_lwe.coding.LadderSwitcher
import cryptography.lattice.ring_lwe.matrix.TwiddleMatrix
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPI
import cryptography.lattice.ring_lwe.ring.RootPP
import math.martix.*

/**
 * Created by CowardlyLion at 2022/2/13 13:42
 *
 * [decreaseCase] is DFT matrix with power decreased by 1
 */
class DftMatrixPPImpl<A>(override val root: RootPP<A>, val primeCase: DftMatrixP<A>, val decreaseCase: DftMatrixPPI<A>) : DftMatrixPP<A>, AbstractSquareFormalProduct<A> {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = super<DftMatrixPP>.elementAtUnsafe(row, column)

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