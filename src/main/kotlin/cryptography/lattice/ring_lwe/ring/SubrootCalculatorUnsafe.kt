package cryptography.lattice.ring_lwe.ring

import math.integer.uint.factored.UIntP
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.UIntPPP
import kotlin.reflect.KClass

/**
 * Created by CowardlyLion at 2022/3/3 22:36
 *
 * 'a' is the order of result
 */
class SubrootCalculatorUnsafe<A> : FactorizationToRoot<RootPPPI<A>, A> {

    companion object {
        val cache = mutableMapOf<KClass<*>, SubrootCalculatorUnsafe<*>>()
        inline fun <reified A> cachedGet() = cache.computeIfAbsent(A::class) { SubrootCalculatorUnsafe<A>() }
    }

    override fun apply(x: RootPPPI<A>, a: UIntP): RootP<A> = RootPImpl(x.root.power(x.order.value / a.value), a)
    override fun apply(x: RootPPPI<A>, a: UIntPP): RootPP<A> = RootPPImpl(x.root.power(x.order.value / a.value), a)
    override fun apply(x: RootPPPI<A>, a: UIntPPP): RootPPP<A> = RootPPPImpl(x.root.power(x.order.value / a.value), a)

    fun maximalPrimePowerSubrootAt(x: RootPPPI<A>, i: UInt): RootPPI<A> = apply(x, x.order.factors[i.toInt()])

    fun allMaximalPrimePowerSubroot(x: RootPPPI<A>): List<RootPPI<A>> = x.order.factors.map { factor -> apply(x, factor) }

}