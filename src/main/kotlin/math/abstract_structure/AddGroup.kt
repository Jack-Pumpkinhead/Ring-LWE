package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/9 13:15
 */
interface AddGroup<A> : AddMonoid<A> {

    override fun hasNegation(a: A): Boolean = true

}