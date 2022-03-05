package util.mapper

import cryptography.lattice.ring_lwe.ring.*

/**
 * Created by CowardlyLion at 2022/3/4 22:26
 */
interface FactorizationToRoot2<X1, X2, A> : BatchFactorizationMapper2<X1, X2, RootP<A>, RootPP<A>, RootPPP<A>, RootPI<A>, RootPPI<A>, RootPPPI<A>> {

}