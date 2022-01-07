package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/7 19:43
 */
abstract class Monoid<A>(val identity: A) {

    abstract fun multiply(x: A, y: A): A

}