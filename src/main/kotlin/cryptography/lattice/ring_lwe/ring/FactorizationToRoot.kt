package cryptography.lattice.ring_lwe.ring

import math.integer.uint.factored.BatchFactorizationMapper

/**
 * Created by CowardlyLion at 2022/3/3 22:26
 */
interface FactorizationToRoot<X, A> : BatchFactorizationMapper<X, RootP<A>, RootPI<A>, RootPP<A>, RootPPI<A>, RootPPP<A>, RootPPPI<A>> {

}