package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import math.abstract_structure.Ring
import math.martix.AbstractConstantValueMatrix
import math.martix.AbstractMatrix
import math.martix.AbstractRowVector
import math.martix.mutable.AbstractMutableMatrix
import math.martix.rowVector
import math.operation.sum

/**
 * Created by CowardlyLion at 2022/1/29 21:02
 */
class RowSummationMatrix<A>(override val ring: Ring<A>, override val size: UInt) : AbstractRowVector<A>, AbstractConstantValueMatrix<A> {

    override val rows: UInt get() = 1u
    override val columns: UInt get() = size
    override val value: A get() = ring.one

    override fun elementAtUnsafe(row: UInt, column: UInt): A = ring.one

    override fun vectorElementAtUnsafe(index: UInt): A = ring.one

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        return ring.rowVector(matrix.columns) { j ->
            ring.sum(matrix.columnVectorViewAt(j))
        }
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        return ring.rowVector(matrix.columns) { j ->
            ring.sum(matrix.columnVectorViewAt(j))
        }
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        dest.setRowUnsafe(0u) { j ->
            ring.sum(matrix.columnVectorViewAt(j))
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        dest.setRowUnsafe(0u) { j ->
            ring.sum(matrix.columnVectorViewAt(j))
        }
    }
}