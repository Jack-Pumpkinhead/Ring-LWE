package math.martix

import math.abstract_structure.CRing
import math.martix.concrete.Constant
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/14 18:06
 */
open class FormalProduct<A>(ring: CRing<A>, val matrices: List<AbstractMatrix<A>>) : AbstractMatrix<A>(ring, matrices.first().rows, matrices.last().columns) {

    init {
        require(matrices.isNotEmpty())
        for (i in 0 until matrices.size - 1) {
            require(matrices[i].columns == matrices[i + 1].rows)
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