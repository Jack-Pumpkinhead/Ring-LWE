package cryptography.lattice.ring_lwe.ring.root

import cryptography.lattice.ring_lwe.ring.*
import math.complex_number.ComplexNumber
import math.complex_number.CyclotomicNumber
import math.complex_number.FieldComplexNumberDouble
import math.integer.uint.factored.UIntP
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.UIntPPP
import util.mapper.FactorizationToRoot1

/**
 * Created by CowardlyLion at 2022/3/4 15:08
 *
 * calculate a complex number e^(2πi*(x/a)) with order a
 *
 * require x, a coprime
 */
object RootCalculatorUnsafeComplexNumber : FactorizationToRoot1<UInt, ComplexNumber<Double>> {

    override fun compute(x: UInt, a: UIntP) = RootPImpl(FieldComplexNumberDouble, CyclotomicNumber(x, a.value), a)
    override fun compute(x: UInt, a: UIntPP) = RootPPImpl(FieldComplexNumberDouble, CyclotomicNumber(x, a.value), a)
    override fun compute(x: UInt, a: UIntPPP) = RootPPPImpl(FieldComplexNumberDouble, CyclotomicNumber(x, a.value), a)

}