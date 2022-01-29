package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.coding.LadderSwitcher
import cryptography.lattice.ring_lwe.matrix.RootDataUIntPrimePower
import cryptography.lattice.ring_lwe.matrix.TwiddleMatrix
import math.abstract_structure.Ring
import math.integer.operation.modTimes
import math.martix.*
import math.martix.mutable.AbstractMutableMatrix
import math.martix.tensor.SquareFormalProduct
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/19 18:09
 */
class DiscreteFourierTransformMatrixPrimePower<A>(val root: RootDataUIntPrimePower<A>, primeCase: DiscreteFourierTransformMatrixPrime<A>) : AbstractSquareMatrix<A> {

    override val ring: Ring<A> get() = root.ring

    override val size: UInt get() = root.order.value

    override fun elementAtUnsafe(row: UInt, column: UInt): A = ring.powerM(root.root, modTimes(row, column, root.order.value))

    val underlyingMatrix: AbstractSquareMatrix<A>

    init {
        underlyingMatrix = if (root.order.power == 1u) primeCase else {
            val subRoot = root.subRootData()
            SquareFormalProduct(
                ring, listOf(
                    ring.permutationMatrix(LadderSwitcher(subRoot.order.prime, subRoot.order.value)),
                    ring.whiskered(subRoot.order.prime, DiscreteFourierTransformMatrixPrimePower(subRoot, primeCase), 1u),
                    TwiddleMatrix(ring, subRoot.order.prime, subRoot.order.value, root.root),
                    ring.whiskered(1u, primeCase, subRoot.order.value)
                )
            )
        }
    }

    override fun determinant(): A = underlyingMatrix.determinant()

    override fun hasInverse(): Boolean = underlyingMatrix.hasInverse()

    override fun inverse(): AbstractSquareMatrix<A> = underlyingMatrix.inverse()

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = underlyingMatrix.timesImpl(matrix)

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = underlyingMatrix.timesRowParallelImpl(matrix)

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        underlyingMatrix.multiplyToImpl(matrix, dest)
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        underlyingMatrix.multiplyToRowParallelImpl(matrix, dest)
    }

}