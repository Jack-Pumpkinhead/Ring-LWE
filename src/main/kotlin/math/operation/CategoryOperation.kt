package math.operation

import math.abstract_structure.Category

/**
 * Created by CowardlyLion at 2022/1/9 19:04
 */

fun <C0, C1> Category<C0, C1>.composeAll(x: List<C1>): C1 = x.reduce { f, g -> compose(f, g) }

/**
 * For testing (id, comp with id, etc.) only.
 * */
fun <C0, C1> Category<C0, C1>.composeAllPrefixedWithIdentity(x: List<C1>): C1 {
    require(x.isNotEmpty())
    var sum = id(source(x[0]))
    for (a in x) {
        sum = compose(sum, a)
    }
    return sum
}