package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/8 12:00
 *
 * Element of [C0E] representing disjoint union of sets in the category of sets.
 * Theoretically ConcreteCategory has richer structure than a true concrete category due to:
 *      1. absence of an equality satisfying function extensionality
 *      2. absence of strict associativity of composition
 */
abstract class FiniteConcreteCategory<C0, C0E, Arr : Arrow<C0, C0E>> {  //C1 = (C0E) -> C0E, the abstract source/target function implemented by an arrow with predefined source and target.


    abstract val descriptions: MutableSet<String>

    abstract fun c0(a: C0E): C0

    abstract val arrows: MutableList<Arr>     //contains working arrows at runtime.
    abstract fun hom(source: C0, target: C0): Collection<Arr>

    abstract fun id(c0: C0): Arr


    fun toCategory(): Category<C0, Arr> = object : Category<C0, Arr> {
        override val descriptions: MutableSet<String> = this@FiniteConcreteCategory.descriptions
        override fun source(f: Arr): C0 = f.source
        override fun target(f: Arr): C0 = f.target
        override fun composeUnsafe(f: Arr, g: Arr): Arr = (f * g) as Arr
        override fun id(c0: C0): Arr = this@FiniteConcreteCategory.id(c0)
    }


}