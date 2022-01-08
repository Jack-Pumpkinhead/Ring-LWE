package math.martix

import math.abstract_structure.Ring
import math.operations.innerProduct

/**
 * Created by CowardlyLion at 2022/1/7 21:35
 */
class OrdinaryMatrix<A>(ring: Ring<A>, val matrix: List<List<A>>) : Matrix<A>(ring, matrix.size.toUInt(), if (matrix.isEmpty()) 0u else matrix[0].size.toUInt()) {

    init {
        for (row in matrix) {
            require(row.size.toUInt() == columns)
        }
    }

    override fun elementAt(row: UInt, column: UInt): A = matrix[row.toInt()][column.toInt()]

    override fun timesImpl(matrix: Matrix<A>): Matrix<A> {
        return when (matrix) {
            is Constant<A>     -> { //n->1->1
                ColumnVector(ring, List(rows.toInt()) { i -> ring.multiply(this.matrix[i][0], matrix.value) })
            }
            is ColumnVector<A> -> { //a->b->1
                ColumnVector(ring, List(rows.toInt()) { i -> ring.innerProduct(this.matrix[i], matrix.vector) })
            }
            is RowVector<A>    -> {  //a->1->b
                ring.matrix(rows, matrix.columns) { i, j ->
                    ring.multiply(this.matrix[i.toInt()][0], matrix.vector[j.toInt()])
                }
            }
            else               -> super.timesImpl(matrix)
        }
    }

    override suspend fun timesRowParallelImpl(matrix: Matrix<A>): Matrix<A> {
        return when (matrix) {
            is ColumnVector<A> -> { //a->b->1
                ColumnVector(ring, List(rows.toInt()) { i -> ring.innerProduct(this.matrix[i], matrix.vector) })
            }
            is RowVector<A>    -> {  //a->1->b
                ring.matrix(rows, matrix.columns) { i, j ->
                    ring.multiply(this.matrix[i.toInt()][0], matrix.vector[j.toInt()])
                }
            }
            else               -> super.timesRowParallelImpl(matrix)
        }
    }

    override fun toString(): String {
        return matrix.joinToString("\n", "[ ", " ]") { row ->
            row.joinToString { it.toString() }
        }
    }
}