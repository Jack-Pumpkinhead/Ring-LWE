package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.coding.LadderSwitcher
import cryptography.lattice.ring_lwe.matrix.RootDataUIntPrimePower
import cryptography.lattice.ring_lwe.matrix.TwiddleMatrix
import math.integer.operation.modTimes
import math.martix.*
import math.martix.mutable.AbstractMutableMatrix
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/19 18:09
 */
class DiscreteFourierTransformMatrixPrimePower<A>(val root: RootDataUIntPrimePower<A>, primeCase: DiscreteFourierTransformMatrixPrime<A>) : AbstractSquareMatrix<A>(root.ring, root.order.primePower) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = ring.powerM(root.root, modTimes(row, column, root.order.primePower))

    val underlyingMatrix: AbstractMatrix<A>

    init {
        underlyingMatrix = if (root.order.power == 1u) primeCase else {
            val subRoot = root.subRootData()
            FormalProduct(
                ring, listOf(
                    ring.permutationMatrix(LadderSwitcher(subRoot.order.prime, subRoot.order.primePower)),
                    ring.whiskered(subRoot.order.prime, DiscreteFourierTransformMatrixPrimePower(subRoot, primeCase), 1u),
                    TwiddleMatrix(ring, subRoot.order.prime, subRoot.order.primePower, root.root),
                    ring.whiskered(1u, primeCase, subRoot.order.primePower)
                )
            )
        }
    }

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = underlyingMatrix.timesImpl(matrix)

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = underlyingMatrix.timesRowParallelImpl(matrix)

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        underlyingMatrix.multiplyToImpl(matrix, dest)
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        underlyingMatrix.multiplyToRowParallelImpl(matrix, dest)
    }

}