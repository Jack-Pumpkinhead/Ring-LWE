package math.integer.modular

/**
 * Created by CowardlyLion at 2022/1/24 12:57
 */

fun UInt.toUIntModular(modulus: UInt): ModularUInt = ModularUInt(modulus, this.mod(modulus))
fun ULong.toULongModular(modulus: ULong): ModularULong = ModularULong(modulus, this.mod(modulus))