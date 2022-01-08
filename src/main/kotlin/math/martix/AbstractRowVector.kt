package math.martix

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/8 17:03
 */
abstract class AbstractRowVector<A>(ring: Ring<A>, columns: UInt) : Matrix<A>(ring, 1u, columns) {

}