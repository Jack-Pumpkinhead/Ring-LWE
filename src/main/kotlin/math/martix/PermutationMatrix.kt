package math.martix

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.Ring
import math.coding.Permutation
import math.martix.concrete.ColumnVector
import math.martix.concrete.Constant
import math.martix.concrete.RowVector
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/16 16:42
 *
 * define matrix F of permutation f satisfying A^i = (FA)^f(i)
 * i.e. F^f(i)_i = 1 and other entry has value 0
 */
class PermutationMatrix<A>(ring: Ring<A>, val f: Permutation) : AbstractMatrix<A>(ring, f.size.uintValue(true), f.size.uintValue(true)) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = if (f(column.toBigInteger()) == row.toBigInteger()) ring.one else ring.zero

    /**
     * (FM)^i_j = M^(f^-1(i))_j
     * */
    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is Constant<A>             -> matrix
        is RowVector<A>            -> matrix
        is AbstractRowVector<A>    -> matrix
        is ColumnVector<A>         -> ring.columnVector(matrix.rows) { i -> matrix.vector[f.inv(i.toBigInteger()).intValue(true)] }
        is AbstractColumnVector<A> -> ring.columnVector(matrix.rows) { i -> matrix.elementAtUnsafe(f.inv(i.toBigInteger()).uintValue(true), 0u) }
        is IdentityMatrix<A>       -> this
        is ZeroMatrix<A>           -> ZeroMatrix(ring, this.rows, matrix.columns)
        else                       -> ring.matrix(matrix.rows, matrix.columns) { i, j -> matrix.elementAtUnsafe(f.inv(i.toBigInteger()).uintValue(true), j) }
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is Constant<A>             -> matrix
        is RowVector<A>            -> matrix
        is AbstractRowVector<A>    -> matrix
        is ColumnVector<A>         -> ring.columnVectorParallel(matrix.rows) { i -> matrix.vector[f.inv(i.toBigInteger()).intValue(true)] }
        is AbstractColumnVector<A> -> ring.columnVectorParallel(matrix.rows) { i -> matrix.elementAtUnsafe(f.inv(i.toBigInteger()).uintValue(true), 0u) }
        is IdentityMatrix<A>       -> this
        is ZeroMatrix<A>           -> ZeroMatrix(ring, this.rows, matrix.columns)
        else                       -> ring.matrixRowParallel(matrix.rows, matrix.columns) { i, j -> matrix.elementAtUnsafe(f.inv(i.toBigInteger()).uintValue(true), j) }
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>             -> dest.setUnsafe(matrix)
            is RowVector<A>            -> dest.setUnsafe(matrix)
            is AbstractRowVector<A>    -> dest.setUnsafe(matrix)
            is ColumnVector<A>         -> dest.indexedSet { i, _ -> matrix.vector[f.inv(i.toBigInteger()).intValue(true)] }
            is AbstractColumnVector<A> -> dest.indexedSet { i, _ -> matrix.elementAtUnsafe(f.inv(i.toBigInteger()).uintValue(true), 0u) }
            is IdentityMatrix<A>       -> dest.setUnsafe(this)
            is ZeroMatrix<A>           -> dest.indexedSet { _, _ -> ring.zero }
            else                       -> dest.indexedSet { i, j -> matrix.elementAtUnsafe(f.inv(i.toBigInteger()).uintValue(true), j) }
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>             -> dest.setUnsafeRowParallel(matrix)
            is RowVector<A>            -> dest.setUnsafeRowParallel(matrix)
            is AbstractRowVector<A>    -> dest.setUnsafeRowParallel(matrix)
            is ColumnVector<A>         -> dest.indexedSetRowParallel { i, _ -> matrix.vector[f.inv(i.toBigInteger()).intValue(true)] }
            is AbstractColumnVector<A> -> dest.indexedSetRowParallel { i, _ -> matrix.elementAtUnsafe(f.inv(i.toBigInteger()).uintValue(true), 0u) }
            is IdentityMatrix<A>       -> dest.setUnsafeRowParallel(this)
            is ZeroMatrix<A>           -> dest.indexedSetRowParallel { _, _ -> ring.zero }
            else                       -> dest.indexedSetRowParallel { i, j -> matrix.elementAtUnsafe(f.inv(i.toBigInteger()).uintValue(true), j) }
        }
    }

    override fun downCast(): AbstractMatrix<A> = when (f.size) {
        BigInteger.ZERO -> EmptyMatrix(ring, rows, columns)
        BigInteger.ONE  -> ring.identityMatrix(1u)
        else            -> super.downCast()
    }

    override fun determinant(): A = if (f.isOddPermutation) ring.negate(ring.one) else ring.one

    override fun hasInverse(): Boolean = true
    override fun inverse() = PermutationMatrix(ring, f.toInverse())

}