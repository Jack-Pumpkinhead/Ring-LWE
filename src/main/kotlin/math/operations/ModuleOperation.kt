package math.operations

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/8 16:33
 */

fun <A> Ring<A>.innerProduct(x: List<A>, y: List<A>): A {
    require(x.size == y.size)
    var sum = zero
    for (i in x.indices) {
        sum = add(sum, multiply(x[i], y[i]))
    }
    return sum
}