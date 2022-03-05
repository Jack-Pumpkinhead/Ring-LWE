package util.mapper

import cryptography.lattice.ring_lwe.ring.*

/**
 * Created by CowardlyLion at 2022/3/3 22:26
 */
interface FactorizationToRoot1<X, A> : BatchFactorizationMapper1<X, RootP<A>, RootPP<A>, RootPPP<A>, RootPI<A>, RootPPI<A>, RootPPPI<A>> {

}