package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/7 19:57
 */
abstract class Group<A>(identity: A) : Monoid<A>(identity) {

    abstract fun inverse(a: A): A

}