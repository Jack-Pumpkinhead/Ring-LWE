package util.mapper

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.*

/**
 * Created by CowardlyLion at 2022/3/4 23:33
 */
interface FactorizationToCe2<X1, X2, A> : BatchFactorizationMapper2<X1, X2, CeMatrixP<A>, CeMatrixPP<A>, CeMatrixPPP<A>, CeMatrixPI<A>, CeMatrixPPI<A>, CeMatrixPPPI<A>> {

}