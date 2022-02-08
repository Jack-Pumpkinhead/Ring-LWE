package math.abstract_structure.module

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/25 15:47
 */
fun <A> Ring<A>.freeModule(base: String, dimension: UInt) = FreeFiniteModule(this, base, dimension)

