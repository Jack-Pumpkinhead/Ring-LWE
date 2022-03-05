package util.mapper

import math.integer.uint.factored.UIntP
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.UIntPPP

/**
 * Created by CowardlyLion at 2022/3/5 12:07
 */
open class FactorizationToRoot1ToDft<X, A>(val f: FactorizationToRoot1<X, A>, val g: RootToDft<A>) : FactorizationToDft1<X, A> {

    override fun compute(x: X, a: UIntP) = g.compute(f.compute(x, a))
    override fun compute(x: X, a: UIntPP) = g.compute(f.compute(x, a))
    override fun compute(x: X, a: UIntPPP) = g.compute(f.compute(x, a))

}