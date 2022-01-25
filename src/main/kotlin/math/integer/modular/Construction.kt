package math.integer.modular

/**
 * Created by CowardlyLion at 2022/1/24 12:57
 */

fun UInt.toUIntModular(modulus:UInt): UIntModular = UIntModular(modulus,this)