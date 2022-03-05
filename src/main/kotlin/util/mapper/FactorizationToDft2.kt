package util.mapper

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.*

/**
 * Created by CowardlyLion at 2022/3/4 22:44
 */
interface FactorizationToDft2<X1, X2, A> : BatchFactorizationMapper2<X1, X2, DftMatrixP<A>, DftMatrixPP<A>, DftMatrixPPP<A>, DftMatrixPI<A>, DftMatrixPPI<A>, DftMatrixPPPI<A>> {

}