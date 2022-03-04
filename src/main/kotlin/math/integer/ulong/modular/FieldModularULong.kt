package math.integer.ulong.modular

import math.abstract_structure.Field

/**
 * Created by CowardlyLion at 2022/1/19 23:34
 */
class FieldModularULong(val prime: ULong) : RingModularULong(prime), Field<ModularULong> {

    override val descriptions: MutableSet<String> = mutableSetOf("field of integer modulo $prime")

    override fun hasInverse(a: ModularULong): Boolean = true

    override val isExactComputation: Boolean get() = true
}