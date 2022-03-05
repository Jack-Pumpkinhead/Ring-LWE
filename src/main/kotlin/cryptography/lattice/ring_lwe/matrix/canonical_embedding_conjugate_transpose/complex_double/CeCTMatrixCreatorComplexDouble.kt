package cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.complex_double

import cryptography.lattice.ring_lwe.ring.root.RootCalculatorUnsafeComplexNumber
import math.complex_number.ComplexNumber
import util.mapper.FactorizationToRoot1ToCeCT

/**
 * Created by CowardlyLion at 2022/3/5 11:50
 */
object CeCTMatrixCreatorComplexDouble : FactorizationToRoot1ToCeCT<UInt, ComplexNumber<Double>>(RootCalculatorUnsafeComplexNumber, CeCTMatrixBuilderComplexDouble)