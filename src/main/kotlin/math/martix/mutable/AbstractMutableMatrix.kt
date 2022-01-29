package math.martix.mutable

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.martix.AbstractMatrix
import math.martix.mutable.view.MutableColumnVectorView
import math.martix.mutable.view.MutableRowVectorView
import math.martix.mutable.view.MutableSubmatrixView
import math.vector.VectorLike

/**
 * Created by CowardlyLion at 2022/1/8 22:21
 */
interface AbstractMutableMatrix<A> : AbstractMatrix<A> {

    fun setElementAt(row: UInt, column: UInt, a: A) {
        require(row < rows)
        require(column < columns)
        setElementAtUnsafe(row, column, a)
    }

    fun setElementAtUnsafe(row: UInt, column: UInt, a: A)

    fun set(matrix: AbstractMatrix<A>) {
        require(this.rows == matrix.rows)
        require(this.columns == matrix.columns)
        indexed { i, j ->
            setElementAtUnsafe(i, j, matrix.elementAt(i, j))
        }
    }

    fun setUnsafe(matrix: AbstractMatrix<A>) {
        indexed { i, j ->
            setElementAtUnsafe(i, j, matrix.elementAt(i, j))
        }
    }

    suspend fun setUnsafeRowParallel(matrix: AbstractMatrix<A>) {
        indexedRowParallel { i, j ->
            setElementAtUnsafe(i, j, matrix.elementAt(i, j))
        }
    }

    fun set(op: (UInt, UInt) -> A) {
        for (i in 0u until rows) {
            for (j in 0u until columns) {
                setElementAtUnsafe(i, j, op(i, j))
            }
        }
    }

    suspend fun setRowParallel(op: (UInt, UInt) -> A) = coroutineScope {
        for (i in 0u until rows) {
            launch {
                for (j in 0u until columns) {
                    setElementAtUnsafe(i, j, op(i, j))
                }
            }
        }
    }


    fun setRowUnsafe(row: UInt, vector: VectorLike<A>) {
        setRowUnsafe(row) { j -> vector.vectorElementAt(j) }
    }

    fun setRowUnsafe(row: UInt, list: List<A>) {
        setRowUnsafe(row) { j -> list[j.toInt()] }
    }

    fun setRowUnsafe(row: UInt, op: (UInt) -> A) {
        for (j in 0u until columns) {
            setElementAtUnsafe(row, j, op(j))
        }
    }

    fun setColumnUnsafe(column: UInt, op: (UInt) -> A) {
        for (i in 0u until rows) {
            setElementAtUnsafe(i, column, op(i))
        }
    }


    fun mutableColumnVectorViewAt(column: UInt) = MutableColumnVectorView(ring, this, column)

    fun mutableRowVectorViewAt(row: UInt) = MutableRowVectorView(ring, this, row)

    fun mutableSubmatrixViewAt(rowBase: UInt, columnBase: UInt, rows: UInt, columns: UInt) = MutableSubmatrixView(ring, this, rowBase, columnBase, rows, columns)


}