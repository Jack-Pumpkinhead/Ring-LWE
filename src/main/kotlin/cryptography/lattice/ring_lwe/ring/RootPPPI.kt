package cryptography.lattice.ring_lwe.ring

import math.abstract_structure.monoid.MonoidElementCachePower
import math.integer.uint.factored.UIntPPPI

/**
 * Created by CowardlyLion at 2022/3/3 20:19
 *
 * require root.order == order.value
 */
interface RootPPPI<A> {

    val root: MonoidElementCachePower<A>

    val order: UIntPPPI

    val inverse: RootPPPI<A>

}