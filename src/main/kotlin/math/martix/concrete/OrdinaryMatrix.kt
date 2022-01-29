package math.martix.concrete

import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.martix.EmptyMatrix
import math.martix.mutable.MutableMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/7 21:35
 */
open class OrdinaryMatrix<A>(override val ring: Ring<A>, override val rows: UInt, override val columns: UInt, val matrix: List<List<A>>) : AbstractMatrix<A> {

    init {
        lazyAssert2 {
            assert(matrix.size.toUInt() >= rows)
            matrix.forEach {
                assert(it.size.toUInt() >= columns)
            }
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix[row.toInt()][column.toInt()]


    override fun rowListAt(row: UInt): List<A> = matrix[row.toInt()]
    override fun rowMutableListAt(row: UInt): MutableList<A> = matrix[row.toInt()].toMutableList()

    override fun downCast(): AbstractMatrix<A> {
        return when {
            rows == 0u || columns == 0u -> return EmptyMatrix(ring, rows, columns)
            rows == 1u && columns == 1u -> Constant(ring, matrix[0][0])
            rows == 1u                  -> RowVector(ring, matrix[0])
            columns == 1u               -> ColumnVector(ring, List(rows.toInt()) { i -> matrix[i][0] })
            else                        -> this
        }
    }

    override fun toMutableMatrix(): MutableMatrix<A> = MutableMatrix(ring, rows, columns, matrix.map { row -> row.toMutableList() })


    override fun toString(): String {
        return matrix.joinToString(",\n", "{\n", "}") { row ->
            row.joinToString(", ", "{", "}") { it.toString() }
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
                if (this.elementAtUnsafe(i, j) != other.elementAtUnsafe(i, j)) return false
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
}