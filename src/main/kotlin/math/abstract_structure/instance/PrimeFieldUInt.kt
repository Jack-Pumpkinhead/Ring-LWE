package math.abstract_structure.instance

import math.abstract_structure.Field
import math.integer.modular.ModularUInt

/**
 * Created by CowardlyLion at 2022/1/26 23:17
 */
class PrimeFieldUInt(val prime: UInt) : RingModularUInt(prime), Field<ModularUInt> {

    override val descriptions: MutableSet<String> = mutableSetOf("field of integer modulo $prime")

    override fun hasInverse(a: ModularUInt): Boolean = true

}