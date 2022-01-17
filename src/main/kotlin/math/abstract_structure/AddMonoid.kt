package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/9 13:15
 */
interface AddMonoid<A> {

    val descriptions: MutableSet<String>        //let instance of abstract structure parametrized by string, so need not implement equals() and hashcode()

    val zero: A
    fun add(x: A, y: A): A

    fun hasNegation(a: A): Boolean
    fun negate(a: A): A


}