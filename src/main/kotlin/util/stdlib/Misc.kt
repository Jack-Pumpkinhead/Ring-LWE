package util.stdlib

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Created by CowardlyLion at 2022/2/3 13:16
 */
infix fun UInt.shl(bitCount: UInt): UInt = this.shl(bitCount.toInt())
infix fun UInt.shr(bitCount: UInt): UInt = this.shr(bitCount.toInt())

fun Double.toString(digits:UInt) = "%.${digits}f".format(this)

@OptIn(ExperimentalContracts::class)
inline fun repeat(times: UInt, action: (UInt) -> Unit) {
    contract { callsInPlace(action) }

    for (index in 0u until times) {
        action(index)
    }
}
