package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/8 13:00
 */
abstract class Category<C0, C1>(val descriptions: MutableSet<String>) {

    abstract fun source(f: C1): C0
    abstract fun target(f: C1): C0


    fun compose(f: C1, g: C1): C1 {
        require(target(f) == source(g))
        val result = composeUnsafe(f, g)
        assert(source(result) == source(f))
        assert(target(result) == target(g))
        return result
    }

    protected abstract fun composeUnsafe(f: C1, g: C1): C1

    abstract fun id(c0: C0): C1


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Category<*, *>) return false

        return other.descriptions.any { this.descriptions.contains(it) }
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }


}
