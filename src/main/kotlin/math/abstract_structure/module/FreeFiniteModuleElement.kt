package math.abstract_structure.module

import math.martix.AbstractColumnVector
import math.vector.VectorLike

/**
 * Created by CowardlyLion at 2022/1/25 14:35
 */
class FreeFiniteModuleElement<A>(val module: FreeFiniteModule<A>, val coordinate: AbstractColumnVector<A>) : VectorLike<A> {

    init {
        require(module.dimension == coordinate.size)
    }

    override val size: UInt get() = coordinate.size

    override fun vectorElementAtUnsafe(index: UInt): A = coordinate.vectorElementAtUnsafe(index)


}