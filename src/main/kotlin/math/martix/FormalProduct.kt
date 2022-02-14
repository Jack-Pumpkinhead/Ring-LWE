package math.martix

import math.abstract_structure.Ring
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/14 18:06
 */
class FormalProduct<A>(override val ring: Ring<A>, override val rows: UInt, override val columns: UInt, override val matrices: List<AbstractMatrix<A>>) : AbstractFormalProduct<A> {

    init {
        lazyAssert2 {
            assert(matrices[0].rows == rows)
            assert(matrices.last().columns == columns)
            for (i in 0 until matrices.size - 1) {
                assert(matrices[i].columns == matrices[i + 1].rows)
            }
        }
    }

}