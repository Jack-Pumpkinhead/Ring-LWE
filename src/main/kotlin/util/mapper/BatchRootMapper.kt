package util.mapper

import cryptography.lattice.ring_lwe.ring.*

/**
 * Created by CowardlyLion at 2022/3/4 18:25
 */
interface BatchRootMapper<A, P : PI, PP : PPI, PPP : PPPI, PI : PPI, PPI : PPPI, PPPI> {

    //TODO implement a cached mapper

    fun compute(a: RootP<A>): P
    fun compute(a: RootPP<A>): PP
    fun compute(a: RootPPP<A>): PPP

    fun compute(a: RootPI<A>): PI =
        when (a) {
            is RootP<A> -> compute(a)
            else        -> error("unknown $a, class: ${a::class}")
        }

    fun compute(a: RootPPI<A>): PPI =
        when (a) {
            is RootP<A>  -> compute(a)
            is RootPP<A> -> compute(a)
            else         -> error("unknown $a, class: ${a::class}")
        }

    fun compute(a: RootPPPI<A>): PPPI =
        when (a) {
            is RootP<A>   -> compute(a)
            is RootPP<A>  -> compute(a)
            is RootPPP<A> -> compute(a)
            else          -> error("unknown $a, class: ${a::class}")
        }

}