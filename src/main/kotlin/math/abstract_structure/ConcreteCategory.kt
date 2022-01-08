package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/8 12:00
 *
 * Element of A representing disjoint union of underlying set of objects in a concrete category.
 */
abstract class ConcreteCategory<A, C0>() {  //C1 = (A) -> A, the abstract source/target function implemented by an arrow with predefined source and target.

    abstract fun c0(a: A): C0


    /**
     * Theoretically ConcreteCategory has richer structure than a true concrete category due to absence of an equality satisfying function extensionality.
     * */
    inner class Arrow(val source: C0, val target: C0, val function: (A) -> A) {

        operator fun times(arrow: Arrow): Arrow {
            require(this.target == arrow.source)
            return Arrow(this.source, arrow.target) { arrow.function(this.function(it)) }
        }

        operator fun A.invoke(arrow: Arrow): A {
            require(arrow.source == c0(this))
            val result = arrow.function(this)
            assert(c0(result) == arrow.target)
            return result
        }

        /*infix fun o(arrow: Arrow): Arrow {
            require(this.source == arrow.target)
            return Arrow(arrow.source, this.target) { this.function(arrow.function(it)) }
        }

        operator fun invoke(a: A): A {
            require(source == c0(a))
            val result = function(a)
            assert(c0(result) == target)
            return result
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

    /**
     * abstract nonsense
     * @return identity arrow over an object
     * */
    fun identity(c0: C0): Arrow = Arrow(c0, c0) { x -> x }

    fun toCategory(): Category<C0, ConcreteCategory<A,C0>.Arrow> = object : Category<C0, ConcreteCategory<A, C0>.Arrow>() {
        override fun source(f: ConcreteCategory<A,C0>.Arrow): C0 = f.source
        override fun target(f: ConcreteCategory<A,C0>.Arrow): C0 = f.target
        override fun compositeImpl(f: ConcreteCategory<A,C0>.Arrow, g: ConcreteCategory<A,C0>.Arrow): ConcreteCategory<A,C0>.Arrow = f * g
        override fun id(c0: C0): ConcreteCategory<A,C0>.Arrow = identity(c0)
    }

}