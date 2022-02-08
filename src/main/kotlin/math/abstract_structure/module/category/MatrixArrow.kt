package math.abstract_structure.module.category

import math.abstract_structure.Arrow
import math.abstract_structure.module.FreeFiniteModule
import math.abstract_structure.module.FreeFiniteModuleElement
import math.martix.AbstractColumnVector
import math.martix.AbstractMatrix

/**
 * Created by CowardlyLion at 2022/1/25 14:44
 */
class MatrixArrow<A>(source: FreeFiniteModule<A>, target: FreeFiniteModule<A>, val matrix: AbstractMatrix<A>) : Arrow<FreeFiniteModule<A>, FreeFiniteModuleElement<A>>(
    source,
    target,
    { x ->
        val matrix1 = matrix * x.coordinate
        if (matrix1 is AbstractColumnVector<A>) {
            target.vector(matrix1)
        } else {
            target.vector(matrix1.columnListAt(0u))
        }
    }
) {

    init {
        require(source.ring == target.ring)
        require(target.ring == matrix.ring)
        require(source.dimension == matrix.columns)
        require(target.dimension == matrix.rows)
    }

}