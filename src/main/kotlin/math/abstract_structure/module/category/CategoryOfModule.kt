package math.abstract_structure.module.category

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/25 17:19
 */
class CategoryOfModule {

    val categoryCache = mutableMapOf<Ring<*>, FiniteConcreteCategoryOfFreeFiniteModule<*>>()

    fun <A> categoryOf(ring: Ring<A>): FiniteConcreteCategoryOfFreeFiniteModule<A> {
        return categoryCache.computeIfAbsent(ring) { FiniteConcreteCategoryOfFreeFiniteModule(ring) } as FiniteConcreteCategoryOfFreeFiniteModule<A>
    }


}