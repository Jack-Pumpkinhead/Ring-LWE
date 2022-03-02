package math.martix

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.Ring
import math.martix.concrete.ColumnVector
import math.martix.concrete.Constant
import math.martix.concrete.RowVector
import math.martix.concrete.view.ColumnVectorView
import math.martix.concrete.view.RowVectorView
import math.martix.mutable.AbstractMutableMatrix
import math.operation.*
import util.stdlib.list
import util.stdlib.mutableList

/**
 * Created by CowardlyLion at 2022/1/7 21:22
 */
interface AbstractMatrix<A> {

    val ring: Ring<A>
    val rows: UInt
    val columns: UInt


    fun elementAt(row: UInt, column: UInt): A {
        require(row < rows)
        require(column < columns)
        return elementAtUnsafe(row, column)
    }

    fun elementAtUnsafe(row: UInt, column: UInt): A  //Kotlin haven't support UInt-indexed array/list yet.


    operator fun times(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        return timesImpl(matrix)
    }

    fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is Constant<A>               -> ring.columnVector(rows) { i -> ring.multiply(elementAtUnsafe(i, 0u), matrix.value) }    //a->1->1
        is AbstractRowVector<A>      -> ring.matrix(this.rows, matrix.columns) { i, j -> ring.multiply(this.elementAtUnsafe(i, 0u), matrix.elementAtUnsafe(0u, j)) }    //a->1->b
        is AbstractColumnVector<A>   -> ring.columnVector(this.rows) { i -> ring.innerProduct(this.rowVectorViewAt(i), matrix) }
        is IdentityMatrix<A>         -> this
        is ZeroMatrix<A>             -> ZeroMatrix(ring, this.rows, matrix.columns)
        is PermutationMatrix<A>      -> {
            //(AF)^i_j = A^i_f(j)
            val result = ring.zeroMutableMatrix(this.rows, matrix.columns)
            for ((x, fx) in matrix.f) {
                for (i in 0u until this.rows) {
                    result.setElementAtUnsafe(i, x, matrix.elementAtUnsafe(i, fx))
                }
            }
            result
        }
        is AbstractDiagonalMatrix<A> -> ring.matrix(rows, columns) { i, j -> ring.multiply(elementAtUnsafe(i, j), matrix.vectorElementAtUnsafe(j)) }
        else                         -> ring.multiplyUnsafe(this, matrix)
    }

    suspend fun timesRowParallel(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        return timesRowParallelImpl(matrix)
    }

    /**
     * Should implement a parallel-by-row matrix multiplication.
     * */
    suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is Constant<A>               -> ring.columnVectorParallel(rows) { i -> ring.multiply(elementAtUnsafe(i, 0u), matrix.value) }    //a->1->1
        is AbstractRowVector<A>      -> ring.matrixRowParallel(this.rows, matrix.columns) { i, j -> ring.multiply(this.elementAtUnsafe(i, 0u), matrix.elementAtUnsafe(0u, j)) }    //a->1->b
        is AbstractColumnVector<A>   -> ring.columnVectorParallel(this.rows) { i -> ring.innerProduct(this.rowVectorViewAt(i), matrix) }
        is IdentityMatrix<A>         -> this
        is ZeroMatrix<A>             -> ZeroMatrix(ring, this.rows, matrix.columns)
        is PermutationMatrix<A>      -> coroutineScope {    //this is column parallel
            //(AF)^i_j = A^i_f(j)
            val result = ring.zeroMutableMatrix(this@AbstractMatrix.rows, matrix.columns)
            for ((x, fx) in matrix.f) {
                launch {
                    for (i in 0u until this@AbstractMatrix.rows) {
                        result.setElementAtUnsafe(i, x, matrix.elementAtUnsafe(i, fx))
                    }
                }
            }
            result
        }
        is AbstractDiagonalMatrix<A> -> ring.matrixRowParallel(rows, columns) { i, j -> ring.multiply(elementAtUnsafe(i, j), matrix.vectorElementAtUnsafe(j)) }
        else                         -> ring.multiplyRowParallelUnsafe(this, matrix)
    }

    fun multiplyToNewMutableMatrix(matrix: AbstractMatrix<A>): AbstractMutableMatrix<A> {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        val result = ring.zeroMutableMatrix(this.rows, matrix.columns)
        this.multiplyToImpl(matrix, result)
        return result
    }

    fun multiplyTo(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        require(this.ring == dest.ring)
        require(this.rows == dest.rows)
        require(matrix.columns == dest.columns)
        multiplyToImpl(matrix, dest)
    }

    fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>               -> dest.set { i, _ -> ring.multiply(elementAtUnsafe(i, 0u), matrix.value) }    //a->1->1
            is AbstractRowVector<A>      -> dest.set { i, j -> ring.multiply(this.elementAtUnsafe(i, 0u), matrix.elementAtUnsafe(0u, j)) }    //a->1->b
            is AbstractColumnVector<A>   -> dest.set { i, _ -> ring.innerProduct(this.rowVectorViewAt(i), matrix) }
            is IdentityMatrix<A>         -> dest.setUnsafe(this)
            is ZeroMatrix<A>             -> dest.set { _, _ -> ring.zero }
            is PermutationMatrix<A>      -> {
                //(AF)^i_j = A^i_f(j)
                for ((x, fx) in matrix.f) {
                    for (i in 0u until this.rows) {
                        dest.setElementAtUnsafe(i, x, matrix.elementAtUnsafe(i, fx))
                    }
                }
            }
            is AbstractDiagonalMatrix<A> -> dest.set { i, j -> ring.multiply(elementAtUnsafe(i, j), matrix.vectorElementAtUnsafe(j)) }
            else                         -> ring.multiplyToUnsafe(this, matrix, dest)
        }
    }

    suspend fun multiplyToNewMutableMatrixRowParallel(matrix: AbstractMatrix<A>): AbstractMutableMatrix<A> {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        val result = ring.zeroMutableMatrix(this.rows, matrix.columns)
        this.multiplyToRowParallelImpl(matrix, result)
        return result
    }

    suspend fun multiplyToRowParallel(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        multiplyToRowParallelImpl(matrix, dest)
    }

    /**
     * Should implement a parallel-by-row matrix multiplication.
     * */
    suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is Constant<A>               -> dest.setRowParallel { i, _ -> ring.multiply(elementAtUnsafe(i, 0u), matrix.value) }    //a->1->1
            is AbstractRowVector<A>      -> dest.setRowParallel { i, j -> ring.multiply(this.elementAtUnsafe(i, 0u), matrix.elementAtUnsafe(0u, j)) }    //a->1->b
            is AbstractColumnVector<A>   -> dest.setRowParallel { i, _ -> ring.innerProduct(this.rowVectorViewAt(i), matrix) }
            is IdentityMatrix<A>         -> dest.setUnsafeRowParallel(this)
            is ZeroMatrix<A>             -> dest.setRowParallel { _, _ -> ring.zero }
            is PermutationMatrix<A>      -> coroutineScope {    //this is column parallel
                //(AF)^i_j = A^i_f(j)
                for ((x, fx) in matrix.f) {
                    launch {
                        for (i in 0u until this@AbstractMatrix.rows) {
                            dest.setElementAtUnsafe(i, x, matrix.elementAtUnsafe(i, fx))
                        }
                    }
                }
            }
            is AbstractDiagonalMatrix<A> -> dest.setRowParallel { i, j -> ring.multiply(elementAtUnsafe(i, j), matrix.vectorElementAtUnsafe(j)) }
            else                         -> ring.multiplyToRowParallelUnsafe(this, matrix, dest)
        }
    }


    //    TODO decide where to put downCast. (before/after multiplication)
    fun downCast(): AbstractMatrix<A> {
        return when {
            this is AbstractMutableMatrix<A> -> this    //TODO mutable row/column vector
            rows == 0u || columns == 0u      -> return EmptyMatrix(ring, rows, columns)
            rows == 1u && columns == 1u      -> Constant(ring, elementAtUnsafe(0u, 0u))
            rows == 1u                       -> RowVector(ring, list(columns) { j -> elementAtUnsafe(0u, j) })
            columns == 1u                    -> ColumnVector(ring, list(rows) { i -> elementAtUnsafe(i, 0u) })
            else                             -> this
        }
    }

    fun rowVectorViewAt(row: UInt): RowVectorView<A> {
        require(row < rows)
        return RowVectorView(ring, this, row)
    }

    fun rowVectorViews(): List<RowVectorView<A>> {
        return list(rows) { row -> RowVectorView(ring, this, row) }
    }

    fun columnVectorViewAt(column: UInt): ColumnVectorView<A> {
        require(column < columns)
        return ColumnVectorView(ring, this, column)
    }

    fun columnVectorViews(): List<ColumnVectorView<A>> {
        return list(columns) { column -> ColumnVectorView(ring, this, column) }
    }

    //modification of sub list/vector should not reflect by this matrix (if result is List, should not cast it to MutableList and modify)
    fun rowListAt(row: UInt): List<A> {
        require(row < rows)
        return list(columns) { i -> elementAtUnsafe(row, i) }
    }

    /**
     * copy of row
     */
    fun rowMutableListAt(row: UInt): MutableList<A> {
        require(row < rows)
        return mutableList(columns) { i -> elementAtUnsafe(row, i) }
    }

    fun columnListAt(column: UInt): List<A> {
        require(column < columns)
        return list(rows) { i -> elementAtUnsafe(i, column) }
    }

    /**
     * copy of column
     */
    fun columnMutableListAt(column: UInt): MutableList<A> {
        require(column < columns)
        return mutableList(rows) { i -> elementAtUnsafe(i, column) }
    }

    fun rowVectorAt(row: UInt): RowVector<A> = RowVector(ring, rowListAt(row))
    fun columnVectorAt(column: UInt): ColumnVector<A> = ColumnVector(ring, columnListAt(column))

    fun toOrdinaryMatrix(): AbstractMatrix<A> = ring.matrix(rows, columns) { i, j -> elementAtUnsafe(i, j) }
    fun toMutableMatrix(): AbstractMutableMatrix<A> = ring.mutableMatrix(rows, columns) { i, j -> elementAtUnsafe(i, j) }


    fun isSquareMatrix(): Boolean = rows == columns


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


}