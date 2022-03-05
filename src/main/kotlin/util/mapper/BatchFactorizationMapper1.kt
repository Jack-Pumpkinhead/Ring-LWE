package util.mapper

import math.integer.uint.factored.*

/**
 * Created by CowardlyLion at 2022/3/3 22:16
 */
interface BatchFactorizationMapper1<X, P : PI, PP : PPI, PPP : PPPI, PI : PPI, PPI : PPPI, PPPI> {

    fun compute(x: X, a: UIntP): P
    fun compute(x: X, a: UIntPP): PP
    fun compute(x: X, a: UIntPPP): PPP

    fun compute(x: X, a: UIntPI): PI =
        when (a) {
            is UIntP -> compute(x, a)
            else     -> error("unknown $a, class: ${a::class}")
        }

    fun compute(x: X, a: UIntPPI): PPI =
        when (a) {
            is UIntP  -> compute(x, a)
            is UIntPP -> compute(x, a)
            else      -> error("unknown $a, class: ${a::class}")
        }

    fun compute(x: X, a: UIntPPPI): PPPI =
        when (a) {
            is UIntP   -> compute(x, a)
            is UIntPP  -> compute(x, a)
            is UIntPPP -> compute(x, a)
            else       -> error("unknown $a, class: ${a::class}")
        }

}