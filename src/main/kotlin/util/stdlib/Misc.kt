package util.stdlib

/**
 * Created by CowardlyLion at 2022/2/3 13:16
 */
infix fun UInt.shl(bitCount: UInt): UInt = this.shl(bitCount.toInt())