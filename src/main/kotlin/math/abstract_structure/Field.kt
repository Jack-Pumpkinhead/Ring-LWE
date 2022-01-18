package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/9 12:57
 */
interface Field<A> : Ring<A> {

    override fun hasInverse(a: A): Boolean = a != zero

}