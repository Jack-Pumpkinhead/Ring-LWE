package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/9 13:15
 */
abstract class AddMonoid<A>(val descriptions: MutableSet<String>, val zero: A) {

    abstract fun add(x: A, y: A): A

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AddMonoid<*>) return false

        if (zero != other.zero) return false
        return other.descriptions.any { this.descriptions.contains(it) }
    }

    override fun hashCode(): Int {
        return zero?.hashCode() ?: 0
    }

}