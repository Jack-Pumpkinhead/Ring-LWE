package util.stdlib

import math.complex_number.ComplexNumber
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Created by CowardlyLion at 2022/2/3 13:16
 */
infix fun UInt.shl(bitCount: UInt) = this.shl(bitCount.toInt())
infix fun UInt.shr(bitCount: UInt) = this.shr(bitCount.toInt())
infix fun ULong.shl(bitCount: UInt) = this.shl(bitCount.toInt())
infix fun ULong.shr(bitCount: UInt) = this.shr(bitCount.toInt())

fun Double.toString(digits: UInt) = "%.${digits}f".format(this)
fun ComplexNumber<Double>.toString(digits: UInt) =
    if (imaginary < 0.0) {
        "${real.toString(digits)} - ${(-imaginary).toString(digits)}i"
    } else {
        "${real.toString(digits)} + ${imaginary.toString(digits)}i"
    }

@OptIn(ExperimentalContracts::class)
inline fun repeat(times: UInt, action: (UInt) -> Unit) {
    contract { callsInPlace(action) }

    for (index in 0u until times) {
        action(index)
    }
}

fun Boolean.toULong() = if (this) 1uL else 0uL