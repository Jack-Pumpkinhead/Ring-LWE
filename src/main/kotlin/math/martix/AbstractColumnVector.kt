package math.martix

import math.martix.concrete.*
import math.martix.mutable.AbstractMutableMatrix
import math.vector.VectorLike
import util.stdlib.list

/**
 * Created by CowardlyLion at 2022/1/8 16:48
 */
interface AbstractColumnVector<A> : AbstractMatrix<A>, VectorLike<A> {

    override val rows: UInt get() = size
    override val columns: UInt get() = 1u

    override fun elementAtUnsafe(row: UInt, column: UInt): A = vectorElementAtUnsafe(row)

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> =
        when (matrix) {
            is ScalarMatrix<A>         -> {  //a->1->1
                ColumnVector(ring, list(size) { i -> ring.multiply(vectorElementAtUnsafe(i), matrix.value) })
            }
            is AbstractColumnVector<A> -> {  //a->1->1
                val value = matrix.vectorElementAtUnsafe(0u)
                ColumnVector(ring, list(size) { i -> ring.multiply(vectorElementAtUnsafe(i), value) })
            }
            is AbstractRowVector<A>    -> {   //a->1->b
                ring.matrix(rows, matrix.columns) { i, j ->
                    ring.multiply(this.vectorElementAtUnsafe(i), matrix.vectorElementAtUnsafe(j))
                }
            }
            else                       -> { //a->1->b
                ring.matrix(rows, matrix.columns) { i, j ->
                    ring.multiply(this.vectorElementAtUnsafe(i), matrix.elementAtUnsafe(0u, j))
                }
            }
        }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> = when (matrix) {
        is ScalarMatrix<A>         -> {  //a->1->1
            ColumnVector(ring, list(size) { i -> ring.multiply(vectorElementAtUnsafe(i), matrix.value) })
        }
        is AbstractColumnVector<A> -> {  //a->1->1
            val value = matrix.vectorElementAtUnsafe(0u)
            ColumnVector(ring, list(size) { i -> ring.multiply(vectorElementAtUnsafe(i), value) })
        }
        is AbstractRowVector<A>    -> {   //a->1->b
            ring.matrixRowParallel(rows, matrix.columns) { i, j ->
                ring.multiply(this.vectorElementAtUnsafe(i), matrix.vectorElementAtUnsafe(j))
            }
        }
        else                       -> { //a->1->b
            ring.matrixRowParallel(rows, matrix.columns) { i, j ->
                ring.multiply(this.vectorElementAtUnsafe(i), matrix.elementAtUnsafe(0u, j))
            }
        }
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is ScalarMatrix<A>         -> {  //a->1->1
                dest.setColumnUnsafe(0u) { i -> ring.multiply(vectorElementAtUnsafe(i), matrix.value) }
            }
            is AbstractColumnVector<A> -> {  //a->1->1
                val value = matrix.vectorElementAtUnsafe(0u)
                dest.setColumnUnsafe(0u) { i -> ring.multiply(vectorElementAtUnsafe(i), value) }
            }
            is AbstractRowVector<A>    -> {   //a->1->b
                dest.set { i, j ->
                    ring.multiply(this.vectorElementAtUnsafe(i), matrix.vectorElementAtUnsafe(j))
                }
            }
            else                       -> { //a->1->b
                dest.set { i, j ->
                    ring.multiply(this.vectorElementAtUnsafe(i), matrix.elementAtUnsafe(0u, j))
                }
            }
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        when (matrix) {
            is ScalarMatrix<A>         -> {  //a->1->1
                dest.setColumnUnsafe(0u) { i -> ring.multiply(vectorElementAtUnsafe(i), matrix.value) }
            }
            is AbstractColumnVector<A> -> {  //a->1->1
                val value = matrix.vectorElementAtUnsafe(0u)
                dest.setColumnUnsafe(0u) { i -> ring.multiply(vectorElementAtUnsafe(i), value) }
            }
            is AbstractRowVector<A>    -> {   //a->1->b
                dest.setRowParallel { i, j ->
                    ring.multiply(this.vectorElementAtUnsafe(i), matrix.vectorElementAtUnsafe(j))
                }
            }
            else                       -> { //a->1->b
                dest.setRowParallel { i, j ->
                    ring.multiply(this.vectorElementAtUnsafe(i), matrix.elementAtUnsafe(0u, j))
                }
            }
        }
    }

    fun subtract(v: AbstractColumnVector<A>): AbstractColumnVector<A> {
        require(this.size == v.size)
        return subtractUnsafe(v)
    }

    fun subtractUnsafe(v: AbstractColumnVector<A>): AbstractColumnVector<A> =
        ring.columnVector(this.size) { i ->
            ring.subtract(this.vectorElementAtUnsafe(i), v.vectorElementAtUnsafe(i))
        }

    fun plus(v: AbstractColumnVector<A>): AbstractColumnVector<A> {
        require(this.size == v.size)
        return plusUnsafe(v)
    }

    fun plusUnsafe(v: AbstractColumnVector<A>): AbstractColumnVector<A> =
        ring.columnVector(this.size) { i ->
            ring.add(this.vectorElementAtUnsafe(i), v.vectorElementAtUnsafe(i))
        }

}