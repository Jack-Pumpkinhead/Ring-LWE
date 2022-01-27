package math.martix.mutable

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.vector.VectorLike

/**
 * Created by CowardlyLion at 2022/1/8 22:21
 */
abstract class AbstractMutableMatrix<A>(ring: Ring<A>, rows: UInt, columns: UInt) : AbstractMatrix<A>(ring, rows, columns) {

    fun setElementAt(row: UInt, column: UInt, a: A) {
        require(row in 0u until rows)
        require(column in 0u until columns)
        setElementAtUnsafe(row, column, a)
    }

    abstract fun setElementAtUnsafe(row: UInt, column: UInt, a: A)

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

    fun indexedSet(op: (UInt, UInt) -> A) {
        for (i in 0u until rows) {
            for (j in 0u until columns) {
                setElementAtUnsafe(i, j, op(i, j))
            }
        }
    }

    suspend fun indexedSetRowParallel(op: (UInt, UInt) -> A) = coroutineScope {
        for (i in 0u until rows) {
            launch {
                for (j in 0u until columns) {
                    setElementAtUnsafe(i, j, op(i, j))
                }
            }
        }
    }

    fun mutableColumnVectorViewAt(column: UInt): AbstractMutableMatrix<A> {
        require(column in 0u until columns)
        return MutableSubmatrixView(ring, this, 0u, column, this.rows, 1u)
    }

    fun mutableRowVectorViewAt(rows: UInt): AbstractMutableMatrix<A> {
        require(rows in 0u until columns)
        return MutableSubmatrixView(ring, this, rows, 0u, 1u, this.columns)
    }


}