package cryptography.lattice.ring_lwe.matrix

import cryptography.lattice.ring_lwe.ring.RootProperPrimePowerUInt
import math.abstract_structure.Ring
import math.coding.LadderIndex
import math.martix.*
import math.martix.concrete.OrdinaryMatrix
import math.martix.mutable.AbstractMutableMatrix
import util.stdlib.lazyAssert2
import util.stdlib.list

/**
 * Created by CowardlyLion at 2022/1/17 16:28
 *
 * r^(b0*b1) = 1
 */
class TwiddleMatrix<A>(override val ring: Ring<A>, override val size: UInt, val b0: UInt, val b1: UInt, val root: RootProperPrimePowerUInt<A>) : AbstractDiagonalMatrix<A> {

    val ladderIndex = LadderIndex(listOf(b0, b1), size)

    init {
        lazyAssert2 {
            assert(size.toULong() == b0.toULong() * b1.toULong())
            assert(size == root.order.value)
        }
    }

    override fun vectorElementAtUnsafe(index: UInt): A {
        val a = ladderIndex.decode(index)
        return root.cachedPower(a[0] * a[1])  //use cached power
//        return ring.powerM(root, a[0] * a[1])
    }

    override val inverse: AbstractSquareMatrix<A>
        get() = TwiddleMatrix(ring, size, b0, b1, root.inverse)

    override fun timesImpl(matrix: AbstractMatrix<A>) = when (matrix) {
        is IdentityMatrix<A> -> this
        is ZeroMatrix<A>     -> ZeroMatrix(ring, this.rows, matrix.columns)
        else                 -> {
            val matrix1 = mutableListOf<List<A>>()
//            println("indexBound: ${ladderIndex.indexBound}, bounds: ${ladderIndex.bounds}")
            for ((i, power) in TwiddleMatrixIterator(ladderIndex)) {   //it happened that 'i' is increased by 1 per step.
//                println("i: $i, power: $power")
                matrix1 += list(matrix.columns) { j -> ring.multiply(root.cachedPower(power), matrix.elementAtUnsafe(i, j)) }
            }
            OrdinaryMatrix(ring, this.rows, matrix.columns, matrix1)
        }
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>) = timesImpl(matrix)

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is IdentityMatrix<A> -> dest.setUnsafe(this)
            is ZeroMatrix<A>     -> dest.set { _, _ -> ring.zero }
            else                 -> {
                for ((i, power) in TwiddleMatrixIterator(ladderIndex)) {
                    dest.setRowUnsafe(i) { j -> ring.multiply(root.cachedPower(power), matrix.elementAtUnsafe(i, j)) }
                }
            }
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        multiplyToImpl(matrix, dest)
    }
}