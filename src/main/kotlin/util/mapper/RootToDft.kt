package util.mapper

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.*

/**
 * Created by CowardlyLion at 2022/3/4 18:27
 */
interface RootToDft<A> : BatchRootMapper<A, DftMatrixP<A>, DftMatrixPP<A>, DftMatrixPPP<A>, DftMatrixPI<A>, DftMatrixPPI<A>, DftMatrixPPPI<A>> {

}