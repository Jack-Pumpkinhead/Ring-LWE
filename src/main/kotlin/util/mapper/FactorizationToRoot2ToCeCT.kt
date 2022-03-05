package util.mapper

import math.integer.uint.factored.UIntP
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.UIntPPP

/**
 * Created by CowardlyLion at 2022/3/5 12:07
 */
open class FactorizationToRoot2ToCeCT<X0, X1, A>(val f: FactorizationToRoot2<X0, X1, A>, val g: RootToCeCT<A>) : FactorizationToCeCT2<X0, X1, A> {

    override fun compute(x0: X0, x1: X1, a: UIntP) = g.compute(f.compute(x0, x1, a))
    override fun compute(x0: X0, x1: X1, a: UIntPP) = g.compute(f.compute(x0, x1, a))
    override fun compute(x0: X0, x1: X1, a: UIntPPP) = g.compute(f.compute(x0, x1, a))

}