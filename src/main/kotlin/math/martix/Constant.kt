package math.martix

import math.abstract_structure.CRing

/**
 * Created by CowardlyLion at 2022/1/8 16:07
 */
class Constant<A>(ring: CRing<A>, val value: A) : AbstractMatrix<A>(ring, 1u, 1u) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = value

}