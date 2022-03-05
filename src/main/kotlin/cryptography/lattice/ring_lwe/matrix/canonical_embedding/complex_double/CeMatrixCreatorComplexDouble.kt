package cryptography.lattice.ring_lwe.matrix.canonical_embedding.complex_double

import cryptography.lattice.ring_lwe.ring.root.RootCalculatorUnsafeComplexNumber
import math.complex_number.ComplexNumber
import util.mapper.FactorizationToRoot1ToCe

/**
 * Created by CowardlyLion at 2022/3/4 23:31
 */
object CeMatrixCreatorComplexDouble : FactorizationToRoot1ToCe<UInt, ComplexNumber<Double>>(RootCalculatorUnsafeComplexNumber, CeMatrixBuilderComplexDouble)
