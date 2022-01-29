package math.martix.concrete

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/8 16:07
 */
class Constant<A>(ring: Ring<A>, value: A) : ScalarMatrix<A>(ring, 1u, value) {

}