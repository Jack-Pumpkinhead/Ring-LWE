package math.martix

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/8 14:00
 */
class ColumnVector<A>(ring: Ring<A>, val vector: List<A>) : AbstractColumnVector<A>(ring, vector.size.toUInt()) {

    override fun elementAt(row: UInt, column: UInt): A = vector[row.toInt()]

    override fun timesImpl(matrix: Matrix<A>): Matrix<A> {  //a->1->b
        return when (matrix) {
            is Constant<A>       -> {  //a->1->1
                ColumnVector(ring, vector.map { ring.multiply(it, matrix.value) })
            }
            is ColumnVector<A>   -> {  //a->1->1
                ColumnVector(ring, vector.map { ring.multiply(it, matrix.vector[0]) })
            }
            is RowVector<A>      -> {
                ring.matrix(rows, matrix.columns) { i, j ->
                    ring.multiply(vector[i.toInt()], matrix.vector[j.toInt()])
                }
            }
            is OrdinaryMatrix<A> -> {
                ring.matrix(rows, matrix.columns) { i, j ->
                    ring.multiply(vector[i.toInt()], matrix.matrix[0][j.toInt()])
                }
            }
            else                 -> {
                ring.matrix(rows, matrix.columns) { i, j ->
                    ring.multiply(vector[i.toInt()], matrix.elementAtSafe(0u, j))
                }
            }
        }
    }

    override suspend fun timesRowParallelImpl(matrix: Matrix<A>): Matrix<A> {   //a->1->b
        return when (matrix) {
            is Constant<A>       -> { //a->1->1
                ColumnVector(ring, listParallel(rows) { i ->
                    ring.multiply(vector[i.toInt()], matrix.value)
                })
            }
            is ColumnVector<A>   -> {  //a->1->1
                ColumnVector(ring, listParallel(rows) { i ->
                    ring.multiply(vector[i.toInt()], matrix.vector[0])
                })
            }
            is RowVector<A>      -> {
                ring.matrixRowParallel(rows, matrix.columns) { i, j ->
                    ring.multiply(vector[i.toInt()], matrix.vector[j.toInt()])
                }
            }
            is OrdinaryMatrix<A> -> {
                ring.matrixRowParallel(rows, matrix.columns) { i, j ->
                    ring.multiply(vector[i.toInt()], matrix.matrix[0][j.toInt()])
                }
            }
            else                 -> {
                ring.matrixRowParallel(rows, matrix.columns) { i, j ->
                    ring.multiply(vector[i.toInt()], matrix.elementAtSafe(0u, j))
                }
            }
        }
    }


}