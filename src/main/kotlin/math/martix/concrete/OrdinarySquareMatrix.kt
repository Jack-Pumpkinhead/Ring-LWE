package math.martix.concrete

import math.abstract_structure.Ring
import math.martix.AbstractSquareMatrix

/**
 * Created by CowardlyLion at 2022/1/29 12:58
 */
class OrdinarySquareMatrix<A>(ring: Ring<A>, override val size: UInt, matrix: List<List<A>>) : OrdinaryMatrix<A>(ring, size, size, matrix), AbstractSquareMatrix<A> {

    override val rows: UInt get() = size
    override val columns: UInt get() = size

    override fun determinant(): A {
        TODO("Not yet implemented")
    }

    override val inverse: AbstractSquareMatrix<A>
        get() {
            TODO("Not yet implemented")
        }


}