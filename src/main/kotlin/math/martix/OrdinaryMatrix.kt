package math.martix

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.CRing
import math.martix.mutable.AbstractMutableMatrix
import math.martix.mutable.MutableMatrix
import math.operations.innerProduct
import math.requireEqualSize
import math.sizeOfFirstRowOrZero

/**
 * Created by CowardlyLion at 2022/1/7 21:35
 */
class OrdinaryMatrix<A>(ring: CRing<A>, val matrix: List<List<A>>) : AbstractMatrix<A>(ring, matrix.size.toUInt(), sizeOfFirstRowOrZero(matrix)) {

    init {
        requireEqualSize(matrix)
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix[row.toInt()][column.toInt()]

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
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

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
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


    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>): AbstractMatrix<A> {
        when (matrix) {
            is Constant<A>     -> { //n->1->1
                for (i in 0u until rows) {
                    dest.setElementAtSafe(i, 0u, ring.multiply(this.matrix[i.toInt()][0], matrix.value))
                }
            }
            is ColumnVector<A> -> { //a->b->1
                for (i in 0u until rows) {
                    dest.setElementAtSafe(i, 0u, ring.innerProduct(this.matrix[i.toInt()], matrix.vector))
                }
            }
            is RowVector<A>    -> {  //a->1->b
                for (i in 0u until rows) {
                    for (j in 0u until matrix.columns) {
                        dest.setElementAtSafe(i, j, ring.multiply(this.matrix[i.toInt()][0], matrix.vector[j.toInt()]))
                    }
                }
            }
            else               -> super.multiplyToImpl(matrix, dest)
        }
        return dest
    }

    override suspend fun multiplyToParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>): AbstractMatrix<A> = coroutineScope {
        when (matrix) {
            is Constant<A>     -> { //n->1->1
                for (i in 0u until this@OrdinaryMatrix.rows) {
                    launch {
                        dest.setElementAtSafe(i, 0u, ring.multiply(this@OrdinaryMatrix.matrix[i.toInt()][0], matrix.value))
                    }
                }
            }
            is ColumnVector<A> -> { //a->b->1
                for (i in 0u until this@OrdinaryMatrix.rows) {
                    launch {
                        dest.setElementAtSafe(i, 0u, ring.innerProduct(this@OrdinaryMatrix.matrix[i.toInt()], matrix.vector))
                    }
                }
            }
            is RowVector<A>    -> {  //a->1->b
                for (i in 0u until this@OrdinaryMatrix.rows) {
                    launch {
                        for (j in 0u until matrix.columns) {
                            dest.setElementAtSafe(i, j, ring.multiply(this@OrdinaryMatrix.matrix[i.toInt()][0], matrix.vector[j.toInt()]))
                        }
                    }
                }
            }
            else               -> super.multiplyToParallelImpl(matrix, dest)
        }
        dest
    }


    override fun downCast(): AbstractMatrix<A> {
        return when {
            rows == 0u || columns == 0u -> return EmptyMatrix(ring, rows, columns)
            rows == 1u && columns == 1u -> Constant(ring, matrix[0][0])
            rows == 1u                  -> RowVector(ring, matrix[0])
            columns == 1u               -> ColumnVector(ring, List(rows.toInt()) { i -> matrix[i][0] })
            else                        -> this
        }
    }

    override fun toMutableMatrix(): MutableMatrix<A> = MutableMatrix(ring, matrix.map { row -> row.toMutableList() })


    override fun toString(): String {
        return matrix.joinToString(",\n", "{\n", "}") { row ->
            row.joinToString(", ","{","}") { it.toString() }
        }
    }
}