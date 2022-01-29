package math.martix.mutable

import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/8 22:49
 */
class MutableMatrix<A>(override val ring: Ring<A>, override val rows: UInt, override val columns: UInt,val matrix: List<MutableList<A>>) : AbstractMutableMatrix<A> {

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

    override fun rowListAt(row: UInt): List<A> {
        return matrix[row.toInt()].toList()
    }

    override fun rowMutableListAt(row: UInt): MutableList<A> {
        return matrix[row.toInt()].toMutableList()
    }

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