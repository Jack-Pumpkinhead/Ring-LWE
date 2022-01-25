package math.abstract_structure.module

import math.abstract_structure.Arrow
import math.martix.AbstractColumnVector
import math.martix.AbstractMatrix

/**
 * Created by CowardlyLion at 2022/1/25 14:44
 */
class MatrixArrow<A>(source: FiniteFreeModuleWithBase<A>, target: FiniteFreeModuleWithBase<A>, val matrix: AbstractMatrix<A>) : Arrow<FiniteFreeModuleWithBase<A>, CoordinateWithBase<A>>(source, target, { vector -> CoordinateWithBase(target.ring, target.base, (matrix * vector.coordinate) as AbstractColumnVector<A>) }) {

    init {
        require(source.ring == target.ring)
        require(target.ring == matrix.ring)
        require(source.dimension == matrix.columns)
        require(target.dimension == matrix.rows)
    }

}