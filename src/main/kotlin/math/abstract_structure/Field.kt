package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/9 12:57
 */
abstract class Field<A>(descriptions: MutableSet<String>, zero: A, one: A) : CRing<A>(descriptions, zero, one) {

    fun inverse(a: A): A {
        require(a != zero)
        return inverseImpl(a)
    }

    abstract fun inverseImpl(a: A): A

}