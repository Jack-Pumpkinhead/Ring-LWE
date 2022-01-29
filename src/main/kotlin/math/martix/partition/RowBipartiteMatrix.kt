package math.martix.partition

import math.martix.AbstractMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/29 22:53
 */
class RowBipartiteMatrix<A>(override val upper: AbstractMatrix<A>, override val lower: AbstractMatrix<A>) : AbstractRowBipartiteMatrix<A> {

    init {
        lazyAssert2 {
            assert(upper.ring == lower.ring)
            assert(upper.columns == lower.columns)
        }
    }

}