package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.coding.LadderSwitcher
import cryptography.lattice.ring_lwe.matrix.TwiddleMatrix
import cryptography.lattice.ring_lwe.ring.RootDataUIntPrimePower
import math.abstract_structure.Ring
import math.integer.operation.modTimes
import math.martix.*
import math.martix.tensor.SquareFormalProduct

/**
 * Created by CowardlyLion at 2022/1/19 18:09
 */
open class DftMatrixPrimePower<A>(val root: RootDataUIntPrimePower<A>, primeCase: DftMatrixPrime<A> = DftMatrixPrime(root.primeSubroot())) : BackingSquareMatrix<A> {

    final override val ring: Ring<A> get() = root.ring

    override fun elementAtUnsafe(row: UInt, column: UInt): A = root.power(modTimes(row, column, root.order.value))

    override val underlyingMatrix: AbstractSquareMatrix<A> =
        if (root.order.power == 1u) primeCase else {
            val subRoot = root.subRootData()
            SquareFormalProduct(
                ring, listOf(
                    ring.permutationMatrix(LadderSwitcher(subRoot.order.prime, subRoot.order.value)),
                    ring.whiskered(subRoot.order.prime, DftMatrixPrimePower(subRoot, primeCase), 1u),
                    TwiddleMatrix(ring, subRoot.order.prime, subRoot.order.value, root.root),
                    ring.whiskered(1u, primeCase, subRoot.order.value)
                )
            )
        }

    override fun hasInverse(): Boolean = ring.hasInverse(ring.ofInteger(size))

    override fun inverse(): AbstractSquareMatrix<A> {
        return ring.formalProduct(DftMatrixPrimePower(root.conjugate()), ring.scalarMatrix(size, ring.inverse(ring.ofInteger(size))))
    }
}