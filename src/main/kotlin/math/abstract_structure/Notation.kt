package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/17 12:31
 */

fun <C0, A> FiniteConcreteCategory<C0, A>.apply(a: A, vararg arrows: Arrow<C0, A>): A {
    if (arrows.isEmpty()) return a
    var result = a
    for (arrow in arrows) {
        require(arrow.source == c0(result))
        result = arrow.applyUnsafe(result)
        require(arrow.target == c0(result))
    }
    return result
}