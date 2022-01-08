package math.martix

import math.abstract_structure.Ring
import math.operations.innerProduct

/**
 * Created by CowardlyLion at 2022/1/8 14:05
 */
class RowVector<A>(ring: Ring<A>, val vector: List<A>) : AbstractRowVector<A>(ring, vector.size.toUInt()) {

    override fun elementAt(row: UInt, column: UInt): A = vector[column.toInt()]

    override fun timesImpl(matrix: Matrix<A>): Matrix<A> = when (matrix) {  //1->a->b
        is Constant<A>     -> Constant(ring, ring.multiply(vector[0], matrix.value))
        is ColumnVector<A> -> Constant(ring, ring.innerProduct(this.vector, matrix.vector))
        /*is RowVector<A>      -> {
            ring.matrix(rows, matrix.columns) { i, j ->
                ring.multiply(vector[i.toInt()], matrix.vector[j.toInt()])
            }
        }
        is OrdinaryMatrix<A> -> {
            ring.matrix(rows, matrix.columns) { i, j ->
                ring.multiply(vector[i.toInt()], matrix.matrix[0][j.toInt()])
            }
        }*/
        else               -> {
            ring.matrix(rows, matrix.columns) { i, j ->
                ring.multiply(vector[i.toInt()], matrix.elementAtSafe(0u, j))
            }
        }
    }

    override suspend fun timesRowParallelImpl(matrix: Matrix<A>): Matrix<A> = times(matrix) //no need to parallelize here.


}