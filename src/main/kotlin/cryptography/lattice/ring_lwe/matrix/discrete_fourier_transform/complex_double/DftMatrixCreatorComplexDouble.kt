package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double

import cryptography.lattice.ring_lwe.ring.root.RootCalculatorUnsafeComplexNumber
import math.complex_number.ComplexNumber
import util.mapper.FactorizationToRoot1ToDft

/**
 * Created by CowardlyLion at 2022/3/4 22:01
 *
 * simply compose two mappers
 *
 * return a DFT matrix with size a
 *
 * root e^(2πi*(x/a)) with order a
 *
 * require x, a coprime
 */
object DftMatrixCreatorComplexDouble : FactorizationToRoot1ToDft<UInt, ComplexNumber<Double>>(RootCalculatorUnsafeComplexNumber, DftMatrixBuilderComplexDouble)
