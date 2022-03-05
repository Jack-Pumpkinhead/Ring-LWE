package util.mapper

import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.*

/**
 * Created by CowardlyLion at 2022/3/5 11:36
 */
interface FactorizationToCeCT1<X, A> : BatchFactorizationMapper1<X, CeCTMatrixP<A>, CeCTMatrixPP<A>, CeCTMatrixPPP<A>, CeCTMatrixPI<A>, CeCTMatrixPPI<A>, CeCTMatrixPPPI<A>> {

}