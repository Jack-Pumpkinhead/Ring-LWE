package math.abstract_structure.monoid

/**
 * Created by CowardlyLion at 2022/3/3 14:32
 */
interface MonoidElementInvertible<A> : MonoidElement<A> {

    val inverse: MonoidElementInvertible<A>

}