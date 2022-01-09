package math.martix.mutable

import math.abstract_structure.CRing
import math.martix.AbstractMatrix

/**
 * Created by CowardlyLion at 2022/1/8 22:21
 */
abstract class AbstractMutableMatrix<A>(ring: CRing<A>, rows: UInt, columns: UInt) : AbstractMatrix<A>(ring, rows, columns) {

    fun setElementAtSafe(row: UInt, column: UInt, a: A) {
        require(row in 0u until rows)
        require(column in 0u until columns)
        setElementAt(row, column, a)
    }

    protected abstract fun setElementAt(row: UInt, column: UInt, a: A)


}