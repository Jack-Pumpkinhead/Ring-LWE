package math.abstract_structure

import util.stdlib.lazyAssert

/**
 * Created by CowardlyLion at 2022/1/8 13:00
 */
interface Category<C0, C1> {

    val descriptions: MutableSet<String>

    fun source(f: C1): C0
    fun target(f: C1): C0
    fun id(c0: C0): C1

    fun compose(f: C1, g: C1): C1 {
        require(target(f) == source(g))
        val result = composeUnsafe(f, g)
        lazyAssert { source(result) == source(f) }
        lazyAssert { target(result) == target(g) }
        return result
    }

    fun composeUnsafe(f: C1, g: C1): C1


    fun toOppositeCategory(): Category<C0, C1> = object : Category<C0, C1> {
        override val descriptions: MutableSet<String> = this@Category.descriptions.mapTo(mutableSetOf()) { "opposite category of ($it)" }
        override fun source(f: C1): C0 = this@Category.target(f)
        override fun target(f: C1): C0 = this@Category.source(f)
        override fun id(c0: C0): C1 = this@Category.id(c0)
        override fun composeUnsafe(f: C1, g: C1): C1 = this@Category.composeUnsafe(g, f)
    }

}
