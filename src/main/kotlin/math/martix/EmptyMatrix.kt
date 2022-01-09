package math.martix

import math.abstract_structure.CRing

/**
 * Created by CowardlyLion at 2022/1/9 15:02
 */
class EmptyMatrix<A>(ring: CRing<A>, rows: UInt, columns: UInt) : AbstractMatrix<A>(ring, rows, columns) {

    init {
        require(rows == 0u || columns == 0u)
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        error("empty matrix have no elements.")
    }

}