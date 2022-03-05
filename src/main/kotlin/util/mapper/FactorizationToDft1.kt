package util.mapper

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.*

/**
 * Created by CowardlyLion at 2022/3/4 17:20
 */
interface FactorizationToDft1<X, A> : BatchFactorizationMapper1<X, DftMatrixP<A>, DftMatrixPP<A>, DftMatrixPPP<A>, DftMatrixPI<A>, DftMatrixPPI<A>, DftMatrixPPPI<A>> {

}