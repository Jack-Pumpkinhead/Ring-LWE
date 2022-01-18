package math.martix

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/8 16:48
 */
abstract class AbstractColumnVector<A>(ring: Ring<A>, rows: UInt) : AbstractMatrix<A>(ring, rows, 1u), VectorLike<A> {

    override fun vectorElementAt(index: UInt): A {
        return elementAt(index, 0u)
    }

    override fun vectorElementAtUnsafe(index: UInt): A {
        return elementAtUnsafe(index, 0u)
    }

}