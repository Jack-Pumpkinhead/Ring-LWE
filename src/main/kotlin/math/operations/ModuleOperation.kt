package math.operations

import math.abstract_structure.Monoid
import math.abstract_structure.CRing

/**
 * Created by CowardlyLion at 2022/1/8 16:33
 */

fun <A> CRing<A>.innerProduct(x: List<A>, y: List<A>): A {
    require(x.size == y.size)
    var sum = zero
    for (i in x.indices) {
        sum = add(sum, multiply(x[i], y[i]))
    }
    return sum
}

fun <A> CRing<A>.sum(x: List<A>): A {
    var sum = zero
    for (a in x) {
        sum = add(sum, a)
    }
    return sum
}

fun <A> CRing<A>.product(x: List<A>): A {
    var sum = one
    for (a in x) {
        sum = multiply(sum, a)
    }
    return sum
}

fun <A> Monoid<A>.product(x: List<A>): A {
    var sum = identity
    for (a in x) {
        sum = multiply(sum, a)
    }
    return sum
}


