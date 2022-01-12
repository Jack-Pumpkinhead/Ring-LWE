package math.martix

import math.abstract_structure.CRing
import math.martix.concrete.ColumnVector
import math.martix.concrete.Constant
import math.martix.concrete.OrdinaryMatrix
import math.martix.concrete.RowVector
import math.martix.concrete.view.ColumnVectorView
import math.martix.concrete.view.RowVectorView
import math.martix.mutable.AbstractMutableMatrix
import math.martix.mutable.MutableMatrix
import math.operations.multiplyToParallelUnsafe
import math.operations.multiplyToUnsafe
import math.operations.multiplyUnsafe
import math.operations.multiplyRowParallelUnsafe

/**
 * Created by CowardlyLion at 2022/1/7 21:22
 */
abstract class AbstractMatrix<A>(val ring: CRing<A>, val rows: UInt, val columns: UInt) {

    init {
        require(rows.toInt() >= 0)
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

    protected open fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = this.ring.multiplyUnsafe(this, matrix)

    suspend fun timesRowParallel(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        return timesRowParallelImpl(matrix)
    }

    /**
     * Should implement a parallel-by-row matrix multiplication.
     * */
    protected open suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = this.ring.multiplyRowParallelUnsafe(this, matrix)

    fun multiplyTo(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        require(this.ring == dest.ring)
        require(this.rows == dest.rows)
        require(matrix.columns == dest.columns)
        multiplyToImpl(matrix, dest)
    }

    protected open fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) = this.ring.multiplyToUnsafe(this, matrix, dest)

    suspend fun multiplyToParallel(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        multiplyToParallelImpl(matrix, dest)
    }

    /**
     * Should implement a parallel-by-row matrix multiplication.
     * */
    protected open suspend fun multiplyToParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) = this.ring.multiplyToParallelUnsafe(this, matrix, dest)


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
    fun determinant(): A {
        TODO()
    }

    fun indexed(op: (UInt, UInt) -> Unit) {
        for (i in 0u until rows) {
            for (j in 0u until columns) {
                op(i, j)
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