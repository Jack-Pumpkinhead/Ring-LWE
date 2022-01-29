package math.martix

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/29 21:00
 */
class ConstantValueMatrix<A>(override val ring: Ring<A>, override val rows: UInt, override val columns: UInt, override val value: A) : AbstractConstantValueMatrix<A> {

}