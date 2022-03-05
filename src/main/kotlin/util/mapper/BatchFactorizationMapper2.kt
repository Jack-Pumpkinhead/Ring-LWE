package util.mapper

import math.integer.uint.factored.*

/**
 * Created by CowardlyLion at 2022/3/4 22:24
 */
interface BatchFactorizationMapper2<X0, X1, P : PI, PP : PPI, PPP : PPPI, PI : PPI, PPI : PPPI, PPPI> {

    fun compute(x0: X0, x1: X1, a: UIntP): P
    fun compute(x0: X0, x1: X1, a: UIntPP): PP
    fun compute(x0: X0, x1: X1, a: UIntPPP): PPP

    fun compute(x0: X0, x1: X1, a: UIntPI): PI =
        when (a) {
            is UIntP -> compute(x0, x1, a)
            else     -> error("unknown $a, class: ${a::class}")
        }

    fun compute(x0: X0, x1: X1, a: UIntPPI): PPI =
        when (a) {
            is UIntP  -> compute(x0, x1, a)
            is UIntPP -> compute(x0, x1, a)
            else      -> error("unknown $a, class: ${a::class}")
        }

    fun compute(x0: X0, x1: X1, a: UIntPPPI): PPPI =
        when (a) {
            is UIntP   -> compute(x0, x1, a)
            is UIntPP  -> compute(x0, x1, a)
            is UIntPPP -> compute(x0, x1, a)
            else       -> error("unknown $a, class: ${a::class}")
        }

}