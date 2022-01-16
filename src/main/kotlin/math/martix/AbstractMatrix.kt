package math.martix

import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.CRing
import math.martix.concrete.ColumnVector
import math.martix.concrete.Constant
import math.martix.concrete.OrdinaryMatrix
import math.martix.concrete.RowVector
import math.martix.concrete.view.ColumnVectorView
import math.martix.concrete.view.RowVectorView
import math.martix.mutable.AbstractMutableMatrix
import math.martix.mutable.MutableMatrix
import math.operations.multiplyToRowParallelUnsafe
import math.operations.multiplyToUnsafe
import math.operations.multiplyUnsafe
import math.operations.multiplyRowParallelUnsafe

/**
 * Created by CowardlyLion at 2022/1/7 21:22
 */
abstract class AbstractMatrix<A>(val ring: CRing<A>, val rows: UInt, val columns: UInt) {

    init {
        require(rows.toInt() >= 0)  //require actural value is a non-negative integer
        require(columns.toInt() >= 0)
    }


    fun elementAt(row: UInt, column: UInt): A {
        require(row in 0u until rows)
        require(column in 0u until columns)
        return elementAtUnsafe(row, column)
    }

    //    no need to be protected, use this to improve performance.
    abstract fun elementAtUnsafe(row: UInt, column: UInt): A  //Kotlin haven't support UInt-indexed array/list yet.


    operator fun times(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        return timesImpl(matrix)
    }

    open fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is Constant<A>             -> ring.columnVector(rows) { i -> ring.multiply(elementAtUnsafe(i, 0u), matrix.value) }    //a->1->1
        is AbstractRowVector<A>    -> ring.matrix(this.rows, matrix.columns) { i, j -> ring.multiply(this.elementAtUnsafe(i, 0u), matrix.elementAtUnsafe(0u, j)) }    //a->1->b
        is AbstractColumnVector<A> -> ring.columnVector(matrix.rows) { i -> this.rowVectorViewAt(i).innerProduct(matrix) }
        is IdentityMatrix<A>       -> this
        is ZeroMatrix<A>           -> ZeroMatrix(ring, this.rows, matrix.columns)
        is PermutationMatrix<A>    -> ring.matrix(rows, columns) { i, j -> elementAtUnsafe(i, matrix.f(j.toBigInteger()).uintValue(true)) }    //(AF)^i_j = A^i_k F^k_j = A^i_f(j)
        else                       -> ring.multiplyUnsafe(this, matrix)
    }

    suspend fun timesRowParallel(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        return timesRowParallelImpl(matrix)
    }

    /**
     * Should implement a parallel-by-row matrix multiplication.
     * */
    open suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is Constant<A>             -> ring.columnVectorParallel(rows) { i -> ring.multiply(elementAtUnsafe(i, 0u), matrix.value) }    //a->1->1
        is AbstractRowVector<A>    -> ring.matrixRowParallel(this.rows, matrix.columns) { i, j -> ring.multiply(this.elementAtUnsafe(i, 0u), matrix.elementAtUnsafe(0u, j)) }    //a->1->b
        is AbstractColumnVector<A> -> ring.columnVectorParallel(matrix.rows) { i -> this.rowVectorViewAt(i).innerProduct(matrix) }
        is IdentityMatrix<A>       -> this
        is ZeroMatrix<A>           -> ZeroMatrix(ring, this.rows, matrix.columns)
        is PermutationMatrix<A>    -> ring.matrixRowParallel(rows, columns) { i, j -> elementAtUnsafe(i, matrix.f(j.toBigInteger()).uintValue(true)) }    //(AF)^i_j = A^i_k F^k_j = A^i_f(j)
        else                       -> ring.multiplyRowParallelUnsafe(this, matrix)
    }

    fun multiplyTo(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        require(this.ring == dest.ring)
        require(this.rows == dest.rows)
        require(matrix.columns == dest.columns)
        multiplyToImpl(matrix, dest)
    }

    protected open fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>             -> dest.indexedSet { i, _ -> ring.multiply(elementAtUnsafe(i, 0u), matrix.value) }    //a->1->1
            is AbstractRowVector<A>    -> dest.indexedSet { i, j -> ring.multiply(this.elementAtUnsafe(i, 0u), matrix.elementAtUnsafe(0u, j)) }    //a->1->b
            is AbstractColumnVector<A> -> dest.indexedSet { i, _ -> this.rowVectorViewAt(i).innerProduct(matrix) }
            is IdentityMatrix<A>       -> dest.setUnsafe(this)
            is ZeroMatrix<A>           -> dest.indexedSet { _, _ -> ring.zero }
            is PermutationMatrix<A>    -> dest.indexedSet { i, j -> elementAtUnsafe(i, matrix.f(j.toBigInteger()).uintValue(true)) }    //(AF)^i_j = A^i_k F^k_j = A^i_f(j)
            else                       -> ring.multiplyToUnsafe(this, matrix, dest)
        }
    }

    suspend fun multiplyToRowParallel(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        multiplyToRowParallelImpl(matrix, dest)
    }

    /**
     * Should implement a parallel-by-row matrix multiplication.
     * */
    protected open suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>             -> dest.indexedSetRowParallel { i, _ -> ring.multiply(elementAtUnsafe(i, 0u), matrix.value) }    //a->1->1
            is AbstractRowVector<A>    -> dest.indexedSetRowParallel { i, j -> ring.multiply(this.elementAtUnsafe(i, 0u), matrix.elementAtUnsafe(0u, j)) }    //a->1->b
            is AbstractColumnVector<A> -> dest.indexedSetRowParallel { i, _ -> this.rowVectorViewAt(i).innerProduct(matrix) }
            is IdentityMatrix<A>       -> dest.setUnsafeRowParallel(this)
            is ZeroMatrix<A>           -> dest.indexedSetRowParallel { _, _ -> ring.zero }
            is PermutationMatrix<A>    -> dest.indexedSetRowParallel { i, j -> elementAtUnsafe(i, matrix.f(j.toBigInteger()).uintValue(true)) }    //(AF)^i_j = A^i_k F^k_j = A^i_f(j)
            else                       -> ring.multiplyToRowParallelUnsafe(this, matrix, dest)
        }
    }


    //    TODO decide where to put downCast. (before/after multiplication)
    open fun downCast(): AbstractMatrix<A> {
        return when {
            this is AbstractMutableMatrix<A> -> this    //TODO mutable row/column vector
            rows == 0u || columns == 0u      -> return EmptyMatrix(ring, rows, columns)
            rows == 1u && columns == 1u      -> Constant(ring, elementAtUnsafe(0u, 0u))
            rows == 1u                       -> RowVector(ring, List(columns.toInt()) { j -> elementAtUnsafe(0u, j.toUInt()) })
            columns == 1u                    -> ColumnVector(ring, List(rows.toInt()) { i -> elementAtUnsafe(i.toUInt(), 0u) })
            else                             -> this
        }
    }

    fun rowVectorViewAt(row: UInt): RowVectorView<A> {
        require(row in 0u until rows)
        return RowVectorView(ring, this, row)
    }

    fun columnVectorViewAt(column: UInt): ColumnVectorView<A> {
        require(column in 0u until columns)
        return ColumnVectorView(ring, this, column)
    }

    //simplification by actual array structure is possible, but may cause problem if underlying array is mutable.
    fun rowVectorAt(row: UInt): RowVector<A> {
        require(row in 0u until rows)
        return RowVector(ring, List(columns.toInt()) { i -> elementAtUnsafe(row, i.toUInt()) })
    }

    fun columnVectorAt(column: UInt): ColumnVector<A> {
        require(column in 0u until columns)
        return ColumnVector(ring, List(rows.toInt()) { i -> elementAtUnsafe(i.toUInt(), column) })
    }

    fun toOrdinaryMatrix(): OrdinaryMatrix<A> = ring.matrix(rows, columns) { i, j -> elementAtUnsafe(i, j) }
    open fun toMutableMatrix(): MutableMatrix<A> = ring.mutableMatrix(rows, columns) { i, j -> elementAtUnsafe(i, j) }

    val isSquareMatrix: Boolean = rows == columns
    open fun determinant(): A {
        TODO()
    }

    open fun hasInverse(): Boolean {
        return determinant() != ring.zero
    }

    open fun inverse(): AbstractMatrix<A> {
        TODO()
    }


    fun indexed(op: (UInt, UInt) -> Unit) {
        for (i in 0u until rows) {
            for (j in 0u until columns) {
                op(i, j)
            }
        }
    }

    suspend fun indexedRowParallel(op: (UInt, UInt) -> Unit) = coroutineScope {
        for (i in 0u until rows) {
            launch {
                for (j in 0u until columns) {
                    op(i, j)
                }
            }
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractMatrix<*>) return false

        if (ring != other.ring) return false
        if (rows != other.rows) return false
        if (columns != other.columns) return false
        for (i in 0u until rows) {
            for (j in 0u until columns) {
                if (this.elementAtUnsafe(i, j) != other.elementAt(i, j)) return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        var result = ring.hashCode()
        result = 31 * result + rows.hashCode()
        result = 31 * result + columns.hashCode()
        for (i in 0u until rows) {
            for (j in 0u until columns) {
                result = 31 * result + elementAtUnsafe(i, j).hashCode()
            }
        }
        return result
    }

    /**
     * compatible with Mathematica's matrix notation
     * */
    override fun toString(): String {
        return List(rows.toInt()) { i ->
            List(columns.toInt()) { j ->
                elementAtUnsafe(i.toUInt(), j.toUInt())
            }
        }.joinToString(",\n", "{\n", "}") { row ->
            row.joinToString(", ", "{", "}") { it.toString() }
        }
    }


}