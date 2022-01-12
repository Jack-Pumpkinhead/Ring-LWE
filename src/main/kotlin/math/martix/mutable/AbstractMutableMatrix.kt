package math.martix.mutable

import math.abstract_structure.CRing
import math.martix.AbstractMatrix

/**
 * Created by CowardlyLion at 2022/1/8 22:21
 */
abstract class AbstractMutableMatrix<A>(ring: CRing<A>, rows: UInt, columns: UInt) : AbstractMatrix<A>(ring, rows, columns) {

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


}