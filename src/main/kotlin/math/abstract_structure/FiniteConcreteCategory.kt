package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/8 12:00
 *
 * Element of A representing disjoint union of sets in the category of sets.
 * Theoretically ConcreteCategory has richer structure than a true concrete category due to:
 *      1. absence of an equality satisfying function extensionality
 *      2. absence of strict associativity of composition
 */
abstract class FiniteConcreteCategory<C0, A> {  //C1 = (A) -> A, the abstract source/target function implemented by an arrow with predefined source and target.


    abstract val descriptions: MutableSet<String>

    abstract fun c0(a: A): C0

    abstract val arrows: List<Arrow<C0, A>>     //contains working arrows at runtime.
    abstract fun hom(source: C0, target: C0): Collection<Arrow<C0, A>>

    fun id(c0: C0): Arrow<C0, A> = Arrow(c0, c0) { x -> x }


    fun toCategory(): Category<C0, Arrow<C0, A>> = object : Category<C0, Arrow<C0, A>> {
        override val descriptions: MutableSet<String> = this@FiniteConcreteCategory.descriptions
        override fun source(f: Arrow<C0, A>): C0 = f.source
        override fun target(f: Arrow<C0, A>): C0 = f.target
        override fun composeUnsafe(f: Arrow<C0, A>, g: Arrow<C0, A>): Arrow<C0, A> = f * g
        override fun id(c0: C0): Arrow<C0, A> = this@FiniteConcreteCategory.id(c0)
    }


}