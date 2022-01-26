package math.abstract_structure.module

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/25 17:19
 */
class CategoryOfModule {

    val categoryCache = mutableMapOf<Ring<*>, FiniteConcreteCategoryOfFiniteFreeModuleWithBaseOverRing<*>>()

    fun <A> categoryOf(ring: Ring<A>): FiniteConcreteCategoryOfFiniteFreeModuleWithBaseOverRing<A> {
        return categoryCache.computeIfAbsent(ring) { FiniteConcreteCategoryOfFiniteFreeModuleWithBaseOverRing(ring) } as FiniteConcreteCategoryOfFiniteFreeModuleWithBaseOverRing<A>
    }


}