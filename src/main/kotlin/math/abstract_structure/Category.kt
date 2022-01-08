package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/8 13:00
 */
abstract class Category<C0, C1> {

    protected abstract fun source(f: C1): C0
    protected abstract fun target(f: C1): C0
    val C1.source: C0
        get() = source(this)
    val C1.target: C0
        get() = target(this)


    fun composite(f: C1, g: C1): C1 {
        require(f.target == g.source)
        val result = compositeImpl(f, g)
        assert(result.source == f.source)
        assert(result.target == g.target)
        return result
    }

    protected abstract fun compositeImpl(f: C1, g: C1): C1

    abstract fun id(c0: C0): C1
}
