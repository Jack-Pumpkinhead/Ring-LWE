package math.integer.uint.factored

import math.integer.uint.modular.FieldModularUInt

/**
 * Created by CowardlyLion at 2022/2/13 21:29
 *
 * represent a prime number or 1
 */
interface UIntPI : UIntPPI {

    override val power: UInt get() = 1u

    override val prime: UInt get() = value

    override val eulerTotient: UInt get() = value - 1u

    override fun coprimeNumberAtUnsafe(i: UInt): UInt = i + 1u

    val primeField get() = FieldModularUInt(value)

}