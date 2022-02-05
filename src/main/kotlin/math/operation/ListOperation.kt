package math.operation

import math.abstract_structure.Monoid
import math.abstract_structure.Ring
import math.abstract_structure.algorithm.powerM
import math.vector.VectorLike

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

fun <A> Ring<A>.innerProduct(x: List<A>, y: VectorLike<A>): A {
    require(x.size.toUInt() == y.size)
    var sum = zero
    for (i in x.indices) {
        sum = add(sum, multiply(x[i], y.vectorElementAtUnsafe(i.toUInt())))
    }
    return sum
}

fun <A> Ring<A>.innerProduct(x: VectorLike<A>, y: VectorLike<A>): A {
    require(x.size == y.size)
    var sum = zero
    for (i in 0u until x.size) {
        sum = add(sum, multiply(x.vectorElementAtUnsafe(i), y.vectorElementAtUnsafe(i)))
    }
    return sum
}

fun <A> Ring<A>.sum(x: List<A>): A {
    var sum = zero
    for (a in x) {
        sum = add(sum, a)
    }
    return sum
}

fun <A> Ring<A>.sum(x: VectorLike<A>): A {
    var sum = zero
    for (i in 0u until x.size) {
        sum = add(sum, x.vectorElementAtUnsafe(i))
    }
    return sum
}

fun <A> Ring<A>.sum(range: UIntRange, x: List<A>): A {
    var sum = zero
    for (i in range) {
        sum = add(sum, x[i.toInt()])
    }
    return sum
}

fun <A> Ring<A>.sum(range: UIntRange, x: (UInt) -> A): A {
    var sum = zero
    for (i in range) {
        sum = add(sum, x(i))
    }
    return sum
}

//if there are any situation that needs fix-time operation (try to prevent timing-attack), create one then.
fun <A> Ring<A>.product(x: List<A>): A {
    var prod = one
    for (a in x) {
        if (a == zero) {
            return zero
        } else {
            prod = multiply(prod, a)
        }
    }
    return prod
}

fun <A> Ring<A>.product(range: UIntRange, x: List<A>): A {
    var prod = one
    for (i in range) {
        val a = x[i.toInt()]
        if (a == zero) {
            return zero
        } else {
            prod = multiply(prod, a)
        }
    }
    return prod
}

fun <A> Ring<A>.product(range: UIntRange, x: (UInt) -> A): A {
    var sum = one
    for (i in range) {
        val a = x(i)
        if (a == zero) {
            return zero
        } else {
            sum = multiply(sum, a)
        }
    }
    return sum
}

fun <A> Ring<A>.powers(a: A, range: UIntRange): List<A> {
    if (range.isEmpty()) return emptyList()
    val list = mutableListOf<A>()
    var x = powerM(a, range.first)
    list += x
    for (i in (range.first + 1u)..range.last) {
        x = multiply(x, a)
        list += x
    }
    return list
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
fun <A> MutableList<A>.expand(size: UInt, defaultElement: A) {
    while (size > this.size.toUInt()) {
        this.add(defaultElement)
    }
}

fun <A> MutableList<A>.shrink(size: UInt) {
    require(size <= this.size.toUInt())
    while (size != this.size.toUInt()) {
        removeLast()
    }
}


