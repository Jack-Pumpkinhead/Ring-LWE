package math.martix

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/20 15:00
 */
abstract class AbstractSquareMatrix<A>(ring: Ring<A>, size: UInt) : AbstractMatrix<A>(ring, size, size) {

    override fun isSquareMatrix(): Boolean = true

    open fun determinant(): A {
        TODO()
    }

    open fun hasInverse(): Boolean {
        return determinant() != ring.zero
    }

    open fun inverse(): AbstractSquareMatrix<A> {
        TODO()
    }

}