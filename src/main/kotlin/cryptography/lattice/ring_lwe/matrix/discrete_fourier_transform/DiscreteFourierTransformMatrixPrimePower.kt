package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import cryptography.lattice.ring_lwe.coding.LadderSwitcher
import cryptography.lattice.ring_lwe.matrix.TwiddleMatrix
import math.integer.operation.modTimes
import math.martix.*
import math.martix.mutable.AbstractMutableMatrix
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/19 18:09
 */
class DiscreteFourierTransformMatrixPrimePower<A>(val root: RootDataULong<A>, primeCase: DiscreteFourierTransformMatrixPrime<A>) : AbstractSquareMatrix<A>(root.ring, root.order.toUInt()) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = ring.powerM(root.root, modTimes(row, column, root.order.toUInt()))

    val underlyingMatrix: AbstractMatrix<A>

    init {
        require(root.orderFactorization.size == 1)
        val order = root.orderFactorization[0]
        underlyingMatrix = if (order.power == 1u) primeCase else {
            val reduceOrder = order.reducePower()
            FormalProduct(
                ring, listOf(
                    ring.permutationMatrix(LadderSwitcher(reduceOrder.prime.toUInt(), reduceOrder.primePower.toUInt())),
                    ring.whiskered(reduceOrder.prime.toBigInteger(), DiscreteFourierTransformMatrixPrimePower(root.subRootData(0u), primeCase), BigInteger.ONE),
                    TwiddleMatrix(ring, reduceOrder.prime.toBigInteger(), reduceOrder.primePower.toBigInteger(), root.root),
                    ring.whiskered(BigInteger.ONE, primeCase, reduceOrder.primePower.toBigInteger())
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