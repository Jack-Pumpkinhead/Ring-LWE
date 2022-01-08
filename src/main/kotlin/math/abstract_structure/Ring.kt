package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/7 19:59
 */
abstract class Ring<A>(val zero: A, val one: A) {

    abstract fun add(x: A, y: A): A
    abstract fun negate(a: A): A
    abstract fun multiply(x: A, y: A): A

    fun toAdditiveGroup(): Group<A> = object : Group<A>(zero) {
        override fun multiply(x: A, y: A): A = add(x, y)
        override fun inverse(a: A): A = negate(a)
    }

    fun toMultiplicativeMonoid(): Monoid<A> = object : Monoid<A>(one) {
        override fun multiply(x: A, y: A): A = this@Ring.multiply(x, y)
    }

}