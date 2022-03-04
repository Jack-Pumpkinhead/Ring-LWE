package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/7 19:43
 */
interface Monoid<A> {

    val descriptions: MutableSet<String>

    val one: A
    fun multiply(x: A, y: A): A

    fun hasInverse(a: A): Boolean
    fun inverse(a: A): A    //throw error if it has no inverse.

    val isExactComputation: Boolean

}