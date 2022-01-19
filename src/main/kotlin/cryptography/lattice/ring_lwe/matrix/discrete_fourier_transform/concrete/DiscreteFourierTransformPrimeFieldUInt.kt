package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DiscreteFourierTransform
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DiscreteFourierTransformMatrix
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.PrimePowerRootCalculator
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.RootData
import math.integer.firstMultiplicativeGeneratorOfPrimeFieldUnsafe
import math.integer.modular.FieldULongModular
import math.integer.modular.ULongModular
import math.integer.operation.modPowerS
import math.integer.primeFactorization

/**
 * Created by CowardlyLion at 2022/1/19 15:25
 */

/**
 * field of ℤ/p has primitive (p-1)-th root of unity, all multiplicative element has order divide (p-1)
 */
@Deprecated("aaa")
suspend fun primeFieldDFTOld(mainPrime: ULong): DiscreteFourierTransform<ULongModular> {
    val primeDec = mainPrime - 1uL
    val factorization = primeDec.primeFactorization()
    val field = FieldULongModular(mainPrime)
    val generator = firstMultiplicativeGeneratorOfPrimeFieldUnsafe(mainPrime)

    return DiscreteFourierTransform(field, object : PrimePowerRootCalculator<ULongModular> {
        /**
         * (g^((m-1)/p^i))^(p^i) = 1 mod m
         */
        override fun primitiveRoot(prime: UInt, power: UInt, primePower: UInt): ULongModular {
            require(power > 0u)
            require(factorization.any { it.prime == prime.toULong() })

            return ULongModular(mainPrime, modPowerS(generator, primeDec / primePower, mainPrime))
        }
    })
}

/**
 * field of ℤ/p has primitive (p-1)-th root of unity, all multiplicative element has order divide (p-1)
 */
suspend fun primeFieldDFT(primeModulus: ULong): DiscreteFourierTransformMatrix<ULongModular> {
    val order = primeModulus - 1uL
    val root = firstMultiplicativeGeneratorOfPrimeFieldUnsafe(primeModulus)
    return DiscreteFourierTransformMatrix(RootData(FieldULongModular(primeModulus), ULongModular(primeModulus, root), order, order.primeFactorization()))
}

