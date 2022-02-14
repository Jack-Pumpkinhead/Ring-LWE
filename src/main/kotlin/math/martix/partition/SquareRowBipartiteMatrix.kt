package math.martix.partition

import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/29 22:57
 */
class SquareRowBipartiteMatrix<A>(override val upper: AbstractMatrix<A>, override val lower: AbstractMatrix<A>) : AbstractSquareRowBipartiteMatrix<A> {

    override val size: UInt get() = upper.columns

    init {
        lazyAssert2 {
            assert(upper.ring == lower.ring)
            assert(upper.columns == lower.columns)
            assert(upper.rows + lower.rows == upper.columns)
        }
    }

    override fun determinant(): A {
        TODO("Not yet implemented")
    }

    override val inverse: AbstractSquareMatrix<A>
        get() {
            TODO("Not yet implemented")
        }


}