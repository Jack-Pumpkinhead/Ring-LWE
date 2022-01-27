package math.martix.concrete

import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.martix.AbstractRowVector
import math.operation.innerProduct

/**
 * Created by CowardlyLion at 2022/1/8 14:05
 */
class RowVector<A>(ring: Ring<A>, val vector: List<A>) : AbstractRowVector<A>(ring, vector.size.toUInt()) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = vector[column.toInt()]
    override fun vectorElementAt(index: UInt): A = vector[index.toInt()]
    override fun vectorElementAtUnsafe(index: UInt): A = vector[index.toInt()]

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> =
        when (matrix) {  //1->a->b
            is Constant<A>     -> Constant(ring, ring.multiply(vector[0], matrix.value))    //1->1->1
            is ColumnVector<A> -> Constant(ring, ring.innerProduct(this.vector, matrix.vector)) //1->a->1
            else               -> super.timesImpl(matrix)   //1->a->b
        }

    override fun rowListAt(row: UInt): List<A> {
        require(row == 0u)
        return vector
    }

    override fun rowMutableListAt(row: UInt): MutableList<A> {
        require(row == 0u)
        return vector.toMutableList()
    }

}