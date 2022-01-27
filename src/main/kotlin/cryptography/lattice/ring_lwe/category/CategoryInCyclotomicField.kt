package cryptography.lattice.ring_lwe.category

import cryptography.lattice.ring_lwe.matrix.ChineseRemainderTransformMatrixPrime
import cryptography.lattice.ring_lwe.matrix.ChineseRemainderTransformMatrixPrimePower
import math.abstract_structure.instance.FieldComplexNumberDouble
import math.abstract_structure.instance.FieldDouble
import math.abstract_structure.instance.RingModularUInt
import math.abstract_structure.instance.RingUInt
import math.abstract_structure.module.CategoryOfModule
import math.abstract_structure.module.FiniteFreeModuleWithBase
import math.abstract_structure.module.freeModule
import math.complex_number.ComplexNumber
import math.integer.FactorizationUIntPrimePower

/**
 * Created by CowardlyLion at 2022/1/26 13:36
 *
 * require [q] be prime
 */
class CategoryInCyclotomicField(val order: FactorizationUIntPrimePower, val q: UInt) {


    val phiOrder = order.eulerTotient()

    val categoies = CategoryOfModule()

    val complexModule = categoies.categoryOf(FieldComplexNumberDouble)
    val realModule = categoies.categoryOf(FieldDouble)
    val zModule = categoies.categoryOf(RingUInt)
    fun modularZModule(modulus: UInt) = categoies.categoryOf(RingModularUInt(modulus))

    /**
     * ℚ(ζp^i) ⊗ ℂ
     */
    val extendedCyclotomicFieldPowerBasis: FiniteFreeModuleWithBase<ComplexNumber<Double>> = FieldComplexNumberDouble.freeModule("${order.value}-th cyclotomic field with power basis", phiOrder)

    val standardBasis = FieldComplexNumberDouble.freeModule("standard basis", phiOrder)


    /**
     * ℚ(ζp^i) ⊗ ℝ
     */
    val cyclotomicFieldPowerBasis = FieldDouble.freeModule("${order.value}-th cyclotomic field with power basis", phiOrder)


    /**
     * ℤ(ζp^i)
     */
    val ringOfIntegerPowerBasis = RingUInt.freeModule("ring of integer of ℚ(ζp^i) with power basis", phiOrder)

    /**
     * dual(ℤ(ζp^i))
     */
    val dualRingOfIntegerPowerBasis = RingUInt.freeModule("dual lattice of ℤ(ζp^i) with basis generated by (1-ζp)/p^i multiply with power basis of ℤ(ζp^i)", phiOrder)
    val dualRingOfIntegerDecodingBasis = RingUInt.freeModule("dual lattice of ℤ(ζp^i) with dual basis of conjugate power basis of ℤ(ζp^i)", phiOrder)


    /**
     * ℤ(ζp^i) / [modulus] * ℤ(ζp^i)
     */
    fun ringOfIntegerPowerBasisModulo(modulus: UInt) = RingModularUInt(modulus).freeModule("ℤ(ζp^i) modulo $modulus with power basis", phiOrder)
    fun ringOfIntegerChineseRemainderBasisModulo(modulus: UInt) = RingModularUInt(modulus).freeModule("ℤ(ζp^i) modulo $modulus with chinese remainder basis", phiOrder)

    /**
     * dual(ℤ(ζp^i)) / [modulus] * dual(ℤ(ζp^i))
     */
    fun dualRingOfIntegerPowerBasisModulo(modulus: UInt) = RingModularUInt(modulus).freeModule("dual(ℤ(ζp^i)) modulo $modulus with basis generated by (1-ζp)/p^i multiply with power basis of ℤ(ζp^i)", phiOrder)
    fun dualRingOfIntegerDecodingBasisModulo(modulus: UInt) = RingModularUInt(modulus).freeModule("dual(ℤ(ζp^i)) modulo $modulus with dual basis of conjugate power basis of ℤ(ζp^i)", phiOrder)
    fun dualRingOfIntegerChineseRemainderBasisModulo(modulus: UInt) = RingModularUInt(modulus).freeModule("dual(ℤ(ζp^i)) modulo $modulus with chinese remainder basis", phiOrder)


    init {
        complexModule.registerBase(extendedCyclotomicFieldPowerBasis)
        complexModule.registerBase(standardBasis)

        val root = FieldComplexNumberDouble.rootDataPrimePower(order)
        complexModule.registerArrow(extendedCyclotomicFieldPowerBasis,standardBasis, ChineseRemainderTransformMatrixPrimePower(root, ChineseRemainderTransformMatrixPrime(root.primeSubroot())))

        realModule.registerBase(cyclotomicFieldPowerBasis)
        zModule.registerBase(ringOfIntegerPowerBasis)
        zModule.registerBase(dualRingOfIntegerPowerBasis)
        zModule.registerBase(dualRingOfIntegerDecodingBasis)


    }

}