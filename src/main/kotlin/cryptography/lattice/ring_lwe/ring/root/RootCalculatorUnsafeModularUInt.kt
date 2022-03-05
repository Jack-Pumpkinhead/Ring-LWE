package cryptography.lattice.ring_lwe.ring.root

import cryptography.lattice.ring_lwe.ring.*
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafeModularUInt
import math.integer.uint.factored.UIntP
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.UIntPPP
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import util.mapper.FactorizationToRoot2

/**
 * Created by CowardlyLion at 2022/3/4 22:11
 */
object RootCalculatorUnsafeModularUInt : FactorizationToRoot2<FieldModularUInt, UInt, ModularUInt> {

    /**
     * additionally require [x1] and [a] coprime, but no check here to improve speed
     */
    override fun compute(x0: FieldModularUInt, x1: UInt, a: UIntP): RootP<ModularUInt> {
        require(a.value > 0u)
        require(x1 < a.value)
        require(x0.primeMinusOne.value.mod(a.value) == 0u)
        val root = SubrootCalculatorUnsafeModularUInt.compute(x0.firstGenerator, a)
        return RootPImpl(root.ring, root.root.power(x1), root.order)
    }

    override fun compute(x0: FieldModularUInt, x1: UInt, a: UIntPP): RootPP<ModularUInt> {
        require(a.value > 0u)
        require(x1 < a.value)
        require(x0.primeMinusOne.value.mod(a.value) == 0u)
        val root = SubrootCalculatorUnsafeModularUInt.compute(x0.firstGenerator, a)
        return RootPPImpl(root.ring, root.root.power(x1), root.order)
    }

    override fun compute(x0: FieldModularUInt, x1: UInt, a: UIntPPP): RootPPP<ModularUInt> {
        require(a.value > 0u)
        require(x1 < a.value)
        require(x0.primeMinusOne.value.mod(a.value) == 0u)
        val root = SubrootCalculatorUnsafeModularUInt.compute(x0.firstGenerator, a)
        return RootPPPImpl(root.ring, root.root.power(x1), root.order)
    }

}