package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/7 19:57
 */
interface Group<A> : Monoid<A> {

    override fun hasInverse(a: A): Boolean = true

    //    x^-1 y^-1 x y
    fun commutator(x: A, y: A): A = multiply(multiply(inverse(x), inverse(y)), multiply(x, y))

    //    x^y := y^-1 x y
    fun actedBy(x: A, y: A): A = multiply(multiply(inverse(y), x), y)

}