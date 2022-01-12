package math.martix.concrete

import math.abstract_structure.CRing
import math.martix.*

/**
 * Created by CowardlyLion at 2022/1/8 14:00
 */
class ColumnVector<A>(ring: CRing<A>, val vector: List<A>) : AbstractColumnVector<A>(ring, vector.size.toUInt()) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = vector[row.toInt()]

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = //a->1->b
        when (matrix) {
            is Constant<A>       -> {  //a->1->1
                ColumnVector(ring, vector.map { ring.multiply(it, matrix.value) })
            }
            is ColumnVector<A>   -> {  //a->1->1
                ColumnVector(ring, vector.map { ring.multiply(it, matrix.vector[0]) })
            }
            is RowVector<A>      -> {   //a->1->b
                ring.matrix(rows, matrix.columns) { i, j ->
                    ring.multiply(vector[i.toInt()], matrix.vector[j.toInt()])
                }
            }
            is OrdinaryMatrix<A> -> {   //a->1->b
                ring.matrix(rows, matrix.columns) { i, j ->
                    ring.multiply(vector[i.toInt()], matrix.matrix[0][j.toInt()])
                }
            }
            else                 -> super.timesImpl(matrix)
        }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {   //a->1->b
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
            is RowVector<A>      -> {   //a->1->b
                ring.matrixRowParallel(rows, matrix.columns) { i, j ->
                    ring.multiply(vector[i.toInt()], matrix.vector[j.toInt()])
                }
            }
            is OrdinaryMatrix<A> -> {   //a->1->b
                ring.matrixRowParallel(rows, matrix.columns) { i, j ->
                    ring.multiply(vector[i.toInt()], matrix.matrix[0][j.toInt()])
                }
            }
            else                 -> super.timesRowParallelImpl(matrix)
        }
    }


}