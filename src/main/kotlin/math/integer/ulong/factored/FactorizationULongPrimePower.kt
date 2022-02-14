package math.integer.ulong.factored

/**
 * Created by CowardlyLion at 2022/1/27 12:38
 */
data class FactorizationULongPrimePower(val prime: ULong, val power: UInt, val primePower: ULong) {

    fun reducePower(): FactorizationULongPrimePower {
        require(power > 1u)
        return FactorizationULongPrimePower(prime, power - 1u, primePower / prime)
    }

    fun eulerTotient(): ULong = when (power) {
        0u   -> 1uL
        1u   -> prime - 1uL
        else -> (primePower / prime) * (prime - 1uL)
    }

    override fun toString(): String {
        return "$prime^$power"
    }
}