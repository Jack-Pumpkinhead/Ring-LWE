package math.martix

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/7 21:22
 */
abstract class Matrix<A>(val ring: Ring<A>, val rows: UInt, val columns: UInt) {

    init {
        require(rows.toInt() >= 0)
        require(columns.toInt() >= 0)
    }

    fun elementAtSafe(row: UInt, column: UInt): A {
        require(row in 0u until rows)
        require(column in 0u until columns)
        return elementAt(row, column)
    }

    protected abstract fun elementAt(row: UInt, column: UInt): A  //Kotlin haven't support UInt-indexed array/list yet.

    operator fun times(matrix: Matrix<A>): Matrix<A> {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        return timesImpl(matrix)
    }

    protected open fun timesImpl(matrix: Matrix<A>): Matrix<A> {
        return ring.matrix(this.rows, matrix.columns) { i, k ->
            var sum = ring.zero
            for (j in 0u until this.columns) {
                sum = ring.add(sum, ring.multiply(this.elementAt(i, j), matrix.elementAt(j, k)))
            }
            sum
        }
    }

    suspend fun timesRowParallel(matrix: Matrix<A>): Matrix<A> {
        require(this.ring == matrix.ring)
        require(this.columns == matrix.rows)
        return timesRowParallelImpl(matrix)
    }

    /**
     * Should implement a parallel by row matrix multiplication.
     * */
    protected open suspend fun timesRowParallelImpl(matrix: Matrix<A>): Matrix<A> {
        return ring.matrixRowParallel(this.rows, matrix.columns) { i, k ->
            var sum = ring.zero
            for (j in 0u until this.columns) {
                sum = ring.add(sum, ring.multiply(this.elementAt(i, j), matrix.elementAt(j, k)))    //maybe faster by caching matrix[i][j], but improvement on speed maybe insignificant.
            }
            sum
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
        return RowVector(ring, List(columns.toInt()) { i -> elementAt(row, i.toUInt()) })
    }

    fun columnVectorAt(column: UInt): ColumnVector<A> {
        require(column in 0u until columns)
        return ColumnVector(ring, List(rows.toInt()) { i -> elementAt(i.toUInt(), column) })
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Matrix<*>) return false

        if (ring != other.ring) return false
        if (rows != other.rows) return false
        if (columns != other.columns) return false
        for (i in 0u until rows) {
            for (j in 0u until columns) {
                if (this.elementAt(i, j) != other.elementAtSafe(i, j)) return false
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
                result = 31 * result + elementAt(i, j).hashCode()
            }
        }
        return result
    }


}