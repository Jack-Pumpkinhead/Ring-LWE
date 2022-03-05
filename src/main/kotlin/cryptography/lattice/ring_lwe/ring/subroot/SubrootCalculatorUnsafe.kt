package cryptography.lattice.ring_lwe.ring.subroot

import cryptography.lattice.ring_lwe.ring.*
import math.integer.uint.factored.UIntP
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.UIntPPP
import util.mapper.FactorizationToRoot1

/**
 * Created by CowardlyLion at 2022/3/3 22:36
 *
 * 'a' is the order of result
 */
interface SubrootCalculatorUnsafe<A> : FactorizationToRoot1<RootPPPI<A>, A> {

    /*companion object {
        val cache = mutableMapOf<KClass<*>, SubrootCalculatorUnsafe<*>>()   //cannot use cache of this type because it lost generic type information
    }*/

    override fun compute(x: RootPPPI<A>, a: UIntP): RootP<A> = RootPImpl(x.ring, x.root.power(x.order.value / a.value), a)
    override fun compute(x: RootPPPI<A>, a: UIntPP): RootPP<A> = RootPPImpl(x.ring, x.root.power(x.order.value / a.value), a)
    override fun compute(x: RootPPPI<A>, a: UIntPPP): RootPPP<A> = RootPPPImpl(x.ring, x.root.power(x.order.value / a.value), a)

    fun maximalPrimePowerSubrootAt(x: RootPPPI<A>, i: UInt): RootPPI<A> = compute(x, x.order.factors[i.toInt()])

    fun allMaximalPrimePowerSubroot(x: RootPPPI<A>): List<RootPPI<A>> = x.order.factors.map { factor -> compute(x, factor) }

    fun subrootReducePowerOne(x: RootPP<A>) =
        if (x.order.power == 2u) {
            RootPImpl(x.ring, x.root.power(x.order.prime), x.order.prime())
        } else {
            RootPPImpl(x.ring, x.root.power(x.order.prime), x.order.reducePowerByOneUnsafe())
        }

    fun subrootPrime(x: RootPP<A>) = compute(x, x.order.prime())


}