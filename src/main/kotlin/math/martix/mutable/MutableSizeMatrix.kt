package math.martix.mutable

import math.abstract_structure.Ring
import math.operation.expand
import math.operation.shrink
import util.stdlib.lazyAssert2
import util.stdlib.mutableList

/**
 * Created by CowardlyLion at 2022/1/10 10:52
 *
 * can dynamically expand/shrink to a suitable size
 *
 * directly manipulate on [matrix]
 */
class MutableSizeMatrix<A>(override val ring: Ring<A>, override var rows: UInt, override var columns: UInt, val matrix: MutableList<MutableList<A>>) : AbstractMutableMatrix<A> {

    init {
        lazyAssert2 {
            assert(matrix.size.toUInt() >= rows)
            matrix.forEach {
                assert(it.size.toUInt() >= columns)
            }
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix[row.toInt()][column.toInt()]

    override fun setElementAtUnsafe(row: UInt, column: UInt, a: A) {
        matrix[row.toInt()][column.toInt()] = a
    }


    fun expand(rows: UInt, columns: UInt, defaultElement: A = ring.zero) {
        if (columns > this.columns) {
            for (row in matrix) {
                row.expand(columns, defaultElement)
            }
        }
        matrix.expand(rows, mutableList(columns) { defaultElement })
        this.rows = rows
        this.columns = columns
    }

    fun expandRow(rows: UInt, defaultElement: A = ring.zero) {
        matrix.expand(rows, mutableList(columns) { defaultElement })
        this.rows = rows
    }

    fun shrinkRow(rows: UInt) {
        matrix.shrink(rows)
        this.rows = rows
    }


}