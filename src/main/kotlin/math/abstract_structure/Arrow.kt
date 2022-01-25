package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/17 13:29
 */
open class Arrow<C0, A>(val source: C0, val target: C0, val function: (A) -> A) {

    operator fun times(arrow: Arrow<C0, A>): Arrow<C0, A> {
        require(this.target == arrow.source)
        return Arrow(this.source, arrow.target) { arrow.function(this.function(it)) }
    }

    fun applyUnsafe(a: A): A = function(a)

//        maybe cannot use this outside the class.
    /*operator fun A.invoke(arrow: Arrow): A {
        require(arrow.source == c0(this))
        val result = arrow.function(this)
        assert(c0(result) == arrow.target)
        return result
    }*/

    /*infix fun o(arrow: Arrow): Arrow {
        require(this.source == arrow.target)
        return Arrow(arrow.source, this.target) { this.function(arrow.function(it)) }
    }*/

    /**
     * There are two types of notation representing arrow composition in a category (and two for function application in a concrete category).
     * Using one notation (reversed function application order) here for clarity.
     * */
    /*fun usageExample(f: Arrow, g: Arrow, a: A) {
        val h = f * g
        val i = g o f
        val x = a(f * g)
        val x1 = (f * g)(a)   //not recommended
        val y = (g o f)(a)
        val y1 = a(g o f)  //not recommended
    }*/

}