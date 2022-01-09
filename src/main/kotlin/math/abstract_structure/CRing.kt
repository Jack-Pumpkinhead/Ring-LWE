package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/7 19:59
 *
 * assuming commutativity of multiplication for convenience
 */
abstract class CRing<A>(descriptions: MutableSet<String>, zero: A, val one: A) : AddGroup<A>(descriptions.mapTo(mutableSetOf()) { "additive group of ($it)" }, zero) {

    abstract fun multiply(x: A, y: A): A

    fun toMultiplicativeMonoid(): Monoid<A> = object : Monoid<A>(descriptions.mapTo(mutableSetOf()) { "multiplicative monoid of $it" }, one) {
        override fun multiply(x: A, y: A): A = this@CRing.multiply(x, y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CRing<*>) return false

        if (zero != other.zero) return false
        if (one != other.one) return false
        return other.descriptions.any { this.descriptions.contains(it) }
    }

    override fun hashCode(): Int {
        var result = zero?.hashCode() ?: 0
        result = 31 * result + (one?.hashCode() ?: 0)
        return result
    }


}