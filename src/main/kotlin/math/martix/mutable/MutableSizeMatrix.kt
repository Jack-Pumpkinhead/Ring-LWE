package math.martix.mutable

import math.abstract_structure.CRing
import math.operations.expand
import math.operations.expandOrShrinkToNewList
import math.operations.shrink

/**
 * Created by CowardlyLion at 2022/1/10 10:52
 *
 * Can dynamically expand/shrink to a suitable size.
 */
class MutableSizeMatrix<A>(ring: CRing<A>, rows: UInt, columns: UInt, val matrix: MutableList<MutableList<A>>) : AbstractMutableMatrix<A>(ring, rows, columns) {

    init {
        checkSize()
    }

    fun checkSize() {
        require(rows <= matrix.size.toUInt())
        if (rows != 0u) {
            for (row in matrix) {
                require(columns <= row.size.toUInt())
            }
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix[row.toInt()][column.toInt()]

    override fun setElementAtUnsafe(row: UInt, column: UInt, a: A) {
        matrix[row.toInt()][column.toInt()] = a
    }


    /**
     * @return matrix that have same underlying data as this, with size changed (possibly filled with a given data)
     * */
    fun expand(rows: UInt, columns: UInt, defaultElement: A = ring.zero): MutableSizeMatrix<A> {
        matrix.expand(rows, MutableList(columns.toInt()) { defaultElement })
        if (columns > this.columns) {
            for (row in matrix) {
                row.expand(columns, defaultElement)
            }
        }
        return MutableSizeMatrix(ring, rows, columns, matrix)
    }

    fun expandRow(rows: UInt, defaultElement: A = ring.zero): MutableSizeMatrix<A> {
        matrix.expand(rows, MutableList(columns.toInt()) { defaultElement })
        return MutableSizeMatrix(ring, rows, columns, matrix)
    }

    /**
     * @return new matrix that make a copy of underlying data with given rows.
     * */
    fun shrinkRow(rows: UInt): MutableSizeMatrix<A> {
        val m = matrix.shrink(rows)
        return MutableSizeMatrix(ring, rows, columns, m)
    }


    /**
     * shrinking matrix would make a copy of the matrix to prevent affecting other MutableSizeMatrix that hold the same matrix instance.
     * @return matrix that have same underlying data as this, with size changed (possibly filled with a given data)
     * */
    fun expandOrShrink(rows: UInt, columns: UInt, defaultElement: A = ring.zero): MutableSizeMatrix<A> {
        val m = matrix.expandOrShrinkToNewList(rows, MutableList(columns.toInt()) { defaultElement })
        for (i in m.indices) {
            m[i] = m[i].expandOrShrinkToNewList(columns, defaultElement)
        }
        return MutableSizeMatrix(ring, rows, columns, m)
    }


}