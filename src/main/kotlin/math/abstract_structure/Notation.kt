package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/17 12:31
 */

fun <C0, C0E, Arr : Arrow<C0, C0E>> FiniteConcreteCategory<C0, C0E, Arr>.apply(a: C0E, vararg arrows: Arr): C0E {
    if (arrows.isEmpty()) return a
    var result = a
    for (arrow in arrows) {
        require(arrow.source == c0(result))
        result = arrow.applyUnsafe(result)
        require(arrow.target == c0(result))
    }
    return result
}
