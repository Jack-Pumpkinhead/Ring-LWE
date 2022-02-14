package math.abstract_structure.module

import math.integer.uint.modular.RingModularUInt
import math.integer.uint.RingUInt
import math.integer.uint.modular.ModularUInt
import util.toUIntModular

/**
 * Created by CowardlyLion at 2022/1/25 17:27
 */

fun quotient(coordinate: FreeFiniteModuleElement<UInt>, modulus: UInt): FreeFiniteModuleElement<ModularUInt> {
    require(coordinate.module.ring == RingUInt)
    val ring = RingModularUInt(modulus)

    return ring.freeModule(coordinate.module.base + "modulo $modulus", coordinate.module.dimension).vector { coordinate.coordinate[it].toUIntModular(modulus) }
}