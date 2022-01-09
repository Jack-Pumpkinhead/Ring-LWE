package math.martix

import math.abstract_structure.CRing

/**
 * Created by CowardlyLion at 2022/1/8 17:03
 */
abstract class AbstractRowVector<A>(ring: CRing<A>, columns: UInt) : AbstractMatrix<A>(ring, 1u, columns) {

}