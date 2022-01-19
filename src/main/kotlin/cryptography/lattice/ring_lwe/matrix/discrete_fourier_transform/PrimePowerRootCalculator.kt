package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

/**
 * Created by CowardlyLion at 2022/1/19 15:15
 */
interface PrimePowerRootCalculator<A> {

    fun primitiveRoot(prime: UInt, power: UInt, primePower:UInt): A

}