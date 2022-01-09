package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/7 19:43
 */
abstract class Monoid<A>(val descriptions: MutableSet<String>, val identity: A) {

    abstract fun multiply(x: A, y: A): A

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Monoid<*>) return false

        if (identity != other.identity) return false
        return other.descriptions.any { this.descriptions.contains(it) }
    }

    override fun hashCode(): Int {
        return identity?.hashCode() ?: 0
    }

}