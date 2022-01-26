package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DiscreteFourierTransformMatrix
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.RootDataULong
import math.integer.firstMultiplicativeGeneratorOfPrimeFieldUnsafe
import math.abstract_structure.instance.FieldModularULong
import math.integer.modular.ULongModular
import math.integer.primeFactorization

/**
 * Created by CowardlyLion at 2022/1/19 15:25
 */

/**
 * field of ℤ/p has primitive (p-1)-th root of unity, all multiplicative element has order divide (p-1)
 */
suspend fun primeFieldDFT(primeModulus: ULong): DiscreteFourierTransformMatrix<ULongModular> {
    val order = primeModulus - 1uL
    val root = firstMultiplicativeGeneratorOfPrimeFieldUnsafe(primeModulus)
    return DiscreteFourierTransformMatrix(RootDataULong(FieldModularULong(primeModulus), ULongModular(primeModulus, root), order, order.primeFactorization()))
}

