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

fun <A> CRing<A>.sum(range: UIntRange, x: List<A>): A {
    var sum = zero
    for (i in range) {
        sum = add(sum, x[i.toInt()])
    }
    return sum
}

fun <A> CRing<A>.sum(range: UIntRange, x: (UInt) -> A): A {
    var sum = zero
    for (i in range) {
        sum = add(sum, x(i))
    }
    return sum
}

fun <A> CRing<A>.product(x: List<A>): A {
    var prod = one
    for (a in x) {
        prod = multiply(prod, a)
    }
    return prod
}

fun <A> CRing<A>.product(range: UIntRange, x: List<A>): A {
    var prod = one
    for (i in range) {
        prod = multiply(prod, x[i.toInt()])
    }
    return prod
}

fun <A> CRing<A>.product(range: UIntRange, x: (UInt) -> A): A {
    var sum = one
    for (i in range) {
        sum = multiply(sum, x(i))
    }
    return sum
}


fun <A> Monoid<A>.product(x: List<A>): A {
    var prod = one
    for (a in x) {
        prod = multiply(prod, a)
    }
    return prod
}

fun <A> Monoid<A>.product(range: UIntRange, x: List<A>): A {
    var sum = one
    for (i in range) {
        sum = multiply(sum, x[i.toInt()])
    }
    return sum
}

fun <A> Monoid<A>.product(range: UIntRange, x: (UInt) -> A): A {
    var sum = one
    for (i in range) {
        sum = multiply(sum, x(i))
    }
    return sum
}

/**
 * make list's size not less than a given number. (list.size >= size)
 * */
fun <A> MutableList<A>.expand(size: UInt, defaultElement: A): MutableList<A> {
    while (size > this.size.toUInt()) {
        this.add(defaultElement)
    }
    return this
}

fun <A> MutableList<A>.shrink(size: UInt): MutableList<A> {
    require(size <= this.size.toUInt())
    return subList(0, size.toInt()).toMutableList()
}


