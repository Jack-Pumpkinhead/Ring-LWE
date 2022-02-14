package math.integer.uint.factored

import util.stdlib.list

/**
 * Created by CowardlyLion at 2022/2/13 11:56
 */
@OptIn(ExperimentalUnsignedTypes::class)
private val twoPowers = UIntArray(32) { i -> 1u.shl(i) }

@OptIn(ExperimentalUnsignedTypes::class)
fun twoPower(power: UInt) = twoPowers[power.toInt()]    //TODO decide if cached two power needed

private val factoredTwoPowers = list(32u) { i ->
    when (i) {
        0u   -> null
        1u   -> PrimeUInt(2u)
        else -> ProperPrimePowerUInt(twoPower(i), 2u, i)
    }
}

/**
 * require power > 0
 */
fun factoredTwoPowerUnsafe(power: UInt) = factoredTwoPowers[power.toInt()]!!