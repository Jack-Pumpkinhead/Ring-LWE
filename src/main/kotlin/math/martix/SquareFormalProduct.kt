package math.martix

import math.abstract_structure.Ring
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/27 16:42
 */
class SquareFormalProduct<A>(override val ring: Ring<A>, override val size: UInt, override val matrices: List<AbstractSquareMatrix<A>>) : AbstractSquareFormalProduct<A> {

    init {
        lazyAssert2 {
            for (m in matrices) {
                assert(m.size == size)
            }
        }
    }

    override val inverse: AbstractSquareMatrix<A> by lazy {
        SquareFormalProduct(ring, size, matrices.reversed().map { it.inverse })
    }

}