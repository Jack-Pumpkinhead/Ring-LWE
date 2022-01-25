package math.abstract_structure.module

import math.abstract_structure.Ring
import math.martix.AbstractColumnVector

/**
 * Created by CowardlyLion at 2022/1/25 14:35
 */
class CoordinateWithBase<A>(val ring: Ring<A>, val base: String, val coordinate: AbstractColumnVector<A>) {

    val dimension get() = coordinate.vectorSize
    val moduleSignature get() = FiniteFreeModuleWithBase(ring, base, dimension)

}