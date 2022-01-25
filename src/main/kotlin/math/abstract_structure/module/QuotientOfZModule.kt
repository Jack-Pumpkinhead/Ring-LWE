package math.abstract_structure.module

import math.abstract_structure.instance.RingModularUInt
import math.abstract_structure.instance.RingUInt
import math.integer.modular.UIntModular
import math.integer.modular.toUIntModular
import math.martix.columnVector

/**
 * Created by CowardlyLion at 2022/1/25 17:27
 */

fun quotient(coordinate: CoordinateWithBase<UInt>, modulus: UInt): CoordinateWithBase<UIntModular> {
    require(coordinate.ring == RingUInt)
    val ring = RingModularUInt(modulus)
    return CoordinateWithBase(ring, coordinate.base + "modulo $modulus", ring.columnVector(coordinate.coordinate.vectorSize) { coordinate.coordinate[it].toUIntModular(modulus) })
}