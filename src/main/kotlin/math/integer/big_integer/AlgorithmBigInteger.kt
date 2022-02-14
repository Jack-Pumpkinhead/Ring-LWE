package math.integer.big_integer

import com.ionspin.kotlin.bignum.integer.BigInteger

/**
 * Created by CowardlyLion at 2022/2/11 16:56
 */
fun BigInteger.ceilLog2(): ULong {
    require(this.isPositive)
    return if (this == BigInteger.ONE) {
        0uL
    } else {
        (this - BigInteger.ONE).maxIndexOfOneULong + 1uL
    }
}