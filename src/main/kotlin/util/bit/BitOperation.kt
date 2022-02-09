package util.bit

/**
 * Created by CowardlyLion at 2022/2/9 12:19
 */
fun UInt.bitAt(i: UInt): Boolean {
    require(i < 32u)
    return this.shr(i.toInt()).and(1u) == 1u
}

/**
 * return another ULong that is one bit different than this
 */
fun UInt.copyWithBitChange(i: UInt, bit: Boolean): UInt {
    require(i < 32u)
    return if (bit) {
        this.or(1u.shl(i.toInt()))
    } else {
        this.inv().or(1u.shl(i.toInt())).inv()
    }
}

fun ULong.bitAt(i: UInt): Boolean {
    require(i < 64u)
    return this.shr(i.toInt()).and(1uL) == 1uL
}

/**
 * return another ULong that is one bit different than this
 */
fun ULong.copyWithBitChange(i: UInt, bit: Boolean): ULong {
    require(i < 64u)
    return if (bit) {
        this.or(1uL.shl(i.toInt()))
    } else {
        this.inv().or(1uL.shl(i.toInt())).inv()
    }
}