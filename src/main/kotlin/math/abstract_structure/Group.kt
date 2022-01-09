package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/7 19:57
 */
abstract class Group<A>(descriptions: MutableSet<String>, identity: A) : Monoid<A>(descriptions, identity) {

    abstract fun inverse(a: A): A

}