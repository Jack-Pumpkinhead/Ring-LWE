package util.mapper

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.*

/**
 * Created by CowardlyLion at 2022/3/4 23:16
 */
interface RootToCe<A> : BatchRootMapper<A, CeMatrixP<A>, CeMatrixPP<A>, CeMatrixPPP<A>, CeMatrixPI<A>, CeMatrixPPI<A>, CeMatrixPPPI<A>> {

}