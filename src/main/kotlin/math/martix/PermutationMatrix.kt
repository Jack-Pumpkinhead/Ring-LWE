package math.martix

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.Ring
import math.coding.permutation.Permutation
import math.martix.concrete.Constant
import math.martix.concrete.OrdinaryMatrix
import math.martix.concrete.RowVector
import math.martix.mutable.AbstractMutableMatrix
import util.stdlib.mutableList

/**
 * Created by CowardlyLion at 2022/1/16 16:42
 *
 * define matrix F of permutation f satisfying A^i = (FA)^f(i)
 * i.e. F^f(i)_i = 1 and other entry has value 0
 */
open class PermutationMatrix<A>(override val ring: Ring<A>, val f: Permutation) : AbstractSquareMatrix<A> {

    override val size: UInt get() = f.size

    override fun elementAtUnsafe(row: UInt, column: UInt): A = if (f(column) == row) ring.one else ring.zero

    /**
     * (FM)^i_j = M^(f^-1(i))_j
     * (FM)^f(i)_j = M^i_j
     * */
    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is Constant<A>             -> matrix
        is RowVector<A>            -> matrix
        is AbstractRowVector<A>    -> matrix
        is AbstractColumnVector<A> -> {
            val result = ring.zeroMutableColumnVector(this.rows)
            for ((i, fi) in f) {
                result.setVectorElementAtUnsafe(fi, matrix.vectorElementAtUnsafe(i))
            }
            result
        }
        is IdentityMatrix<A>       -> this
        is ZeroMatrix<A>           -> ZeroMatrix(ring, this.rows, matrix.columns)
        else                       -> {
            val list = mutableList<List<A>>(this.rows) { emptyList() }
            for ((i, fi) in f) {
                list[fi.toInt()] = matrix.rowListAt(i)
            }
            OrdinaryMatrix(ring, this.rows, matrix.columns, list)
        }
    }

    //    already row parallel
    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = timesImpl(matrix)

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>             -> dest.setUnsafe(matrix)
            is RowVector<A>            -> dest.setUnsafe(matrix)
            is AbstractRowVector<A>    -> dest.setUnsafe(matrix)
            is AbstractColumnVector<A> -> {
                for ((i, fi) in f) {
                    dest.setElementAtUnsafe(fi, 0u, matrix.vectorElementAtUnsafe(i))
                }
            }
            is IdentityMatrix<A>       -> dest.setUnsafe(this)
            is ZeroMatrix<A>           -> dest.set { _, _ -> ring.zero }
            else                       -> {
                for ((i, fi) in f) {
                    for (j in 0u until matrix.columns) {
                        dest.setElementAtUnsafe(fi, j, matrix.elementAtUnsafe(i, j))
                    }
                }
            }
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) = coroutineScope {
        when (matrix) {
            is Constant<A>             -> dest.setUnsafe(matrix)
            is RowVector<A>            -> dest.setUnsafe(matrix)
            is AbstractRowVector<A>    -> dest.setUnsafe(matrix)
            is AbstractColumnVector<A> -> {
                for ((i, fi) in f) {
                    dest.setElementAtUnsafe(fi, 0u, matrix.vectorElementAtUnsafe(i))
                }
            }
            is IdentityMatrix<A>       -> dest.setUnsafe(this@PermutationMatrix)
            is ZeroMatrix<A>           -> dest.set { _, _ -> ring.zero }
            else                       -> {
                for ((i, fi) in f) {
                    launch {
                        for (j in 0u until matrix.columns) {
                            dest.setElementAtUnsafe(fi, j, matrix.elementAtUnsafe(i, j))
                        }
                    }
                }
            }
        }
    }

    override fun downCast(): AbstractMatrix<A> = when (f.size) {
        0u   -> EmptyMatrix(ring, rows, columns)
        1u   -> ring.identityMatrix(1u)
        else -> super.downCast()
    }

    override fun determinant(): A = if (f.isOddPermutation) ring.negate(ring.one) else ring.one

    override fun hasInverse(): Boolean = true
    override val inverse: AbstractSquareMatrix<A> by lazy {
        PermutationMatrix(ring, f.inverse)
    }

}