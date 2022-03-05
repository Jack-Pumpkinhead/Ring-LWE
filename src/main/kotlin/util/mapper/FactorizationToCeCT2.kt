package util.mapper

import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.*

/**
 * Created by CowardlyLion at 2022/3/5 11:37
 */
interface FactorizationToCeCT2<X1, X2, A> : BatchFactorizationMapper2<X1, X2, CeCTMatrixP<A>, CeCTMatrixPP<A>, CeCTMatrixPPP<A>, CeCTMatrixPI<A>, CeCTMatrixPPI<A>, CeCTMatrixPPPI<A>> {

}