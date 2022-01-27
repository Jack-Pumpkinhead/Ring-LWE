package math.martix.tensor

import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.concrete.Constant
import math.martix.mutable.AbstractMutableMatrix
import math.operation.product
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/27 16:42
 */
open class SquareFormalProduct<A>(ring: Ring<A>, val matrices: List<AbstractSquareMatrix<A>>) : AbstractSquareMatrix<A>(ring, matrices.first().rows) {

    init {
//        require(matrices.isNotEmpty())    //already checks in matrices.first()

        lazyAssert2 {
            for (i in 1 until matrices.size) {
                assert(matrices[i].rows == super.rows)
            }
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        return if (matrices.size == 1) {
            matrices[0].elementAtUnsafe(row, column)
        } else {
            var x = matrices.last().columnVectorViewAt(column) as AbstractMatrix<A>
            for (i in matrices.size - 2 downTo 1) {
                x = matrices[i] * x
            }
            x = matrices[0].rowVectorViewAt(row) * x
            (x.downCast() as Constant<A>).value     //TODO add a function 'multiply to vector' in AbstractMatrix
        }
    }

    override fun determinant(): A = ring.product(matrices.map { it.determinant() })

    override fun hasInverse(): Boolean {
        return matrices.all { it.hasInverse() }
    }

    override fun inverse(): AbstractSquareMatrix<A> {
        return SquareFormalProduct(ring, matrices.reversed().map { it.inverse() })
    }

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        var x = matrix
        for (i in matrices.size - 1 downTo 0) {
            x = matrices[i].timesImpl(x)
        }
        return x
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        var x = matrix
        for (i in matrices.size - 1 downTo 0) {
            x = matrices[i].timesRowParallelImpl(x)
        }
        return x
    }


    //    TODO move (and correct) memory-saving code in (History)FormalKroneckerProduct to here.
    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        dest.setUnsafe(timesImpl(matrix))
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        dest.setUnsafeRowParallel(timesRowParallelImpl(matrix))
    }

    override fun downCast(): AbstractMatrix<A> {
        return when (matrices.size) {
            1    -> matrices[0]
            else -> super.downCast()
        }
    }
}