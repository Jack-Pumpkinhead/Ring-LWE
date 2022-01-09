package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/9 13:15
 */
abstract class AddGroup<A>(descriptions: MutableSet<String>, zero: A) : AddMonoid<A>(descriptions, zero) {

    abstract fun negate(a: A): A

}