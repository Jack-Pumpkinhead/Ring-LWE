package math.martix

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/8 16:07
 */
class Constant<A>(ring: Ring<A>, val value: A) : Matrix<A>(ring, 1u, 1u) {

    override fun elementAt(row: UInt, column: UInt): A = value

}