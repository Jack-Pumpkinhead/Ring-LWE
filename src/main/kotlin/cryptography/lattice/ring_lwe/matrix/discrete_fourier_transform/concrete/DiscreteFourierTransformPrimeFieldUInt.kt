package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete

import cryptography.lattice.ring_lwe.matrix.RootDataUInt
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DiscreteFourierTransformMatrix
import math.abstract_structure.instance.PrimeFieldUInt
import math.integer.firstMultiplicativeGeneratorOfPrimeFieldUnsafe
import math.integer.modular.ModularUInt
import math.integer.primeFactorization

/**
 * Created by CowardlyLion at 2022/1/19 15:25
 */

/**
 * field of ℤ/p has primitive (p-1)-th root of unity, all multiplicative element has order divide (p-1)
 */
suspend fun PrimeFieldUInt.dft(): DiscreteFourierTransformMatrix<ModularUInt> {
    val root = firstMultiplicativeGeneratorOfPrimeFieldUnsafe(prime)
    val order = prime - 1u
    return DiscreteFourierTransformMatrix(RootDataUInt(this, ModularUInt(prime, root), order.primeFactorization()))
}
