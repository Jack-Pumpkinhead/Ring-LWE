package math.martix.concrete

import math.abstract_structure.CRing
import math.martix.AbstractMatrix

/**
 * Created by CowardlyLion at 2022/1/8 16:07
 */
class Constant<A>(ring: CRing<A>, val value: A) : AbstractMatrix<A>(ring, 1u, 1u) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = value

}