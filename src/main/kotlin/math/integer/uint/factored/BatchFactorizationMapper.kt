package math.integer.uint.factored

/**
 * Created by CowardlyLion at 2022/3/3 22:16
 */
interface BatchFactorizationMapper<X, P : PI, PI : PPI, PP : PPI, PPI : PPPI, PPP : PPPI, PPPI> {

    fun apply(x: X, a: UIntP): P
    fun apply(x: X, a: UIntPP): PP
    fun apply(x: X, a: UIntPPP): PPP

    fun apply(x: X, a: UIntPI): PI =
        when (a) {
            is UIntP -> apply(x, a)
            else     -> error("unknown $a, class: ${a::class}")
        }

    fun apply(x: X, a: UIntPPI): PPI =
        when (a) {
            is UIntP  -> apply(x, a)
            is UIntPP -> apply(x, a)
            else      -> error("unknown $a, class: ${a::class}")
        }

    fun apply(x: X, a: UIntPPPI): PPPI =
        when (a) {
            is UIntP   -> apply(x, a)
            is UIntPP  -> apply(x, a)
            is UIntPPP -> apply(x, a)
            else       -> error("unknown $a, class: ${a::class}")
        }

}