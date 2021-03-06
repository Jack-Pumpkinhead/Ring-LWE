package math

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlin.math.PI
import kotlin.math.sqrt

/**
 * Created by CowardlyLion at 2022/2/9 12:16
 */
val twoPower31 = 1u.shl(31)
val twoPower32 = 1uL.shl(32)
val twoPower64 = BigInteger.ONE.shl(64)
const val pi2 = PI * 2
val num64 = BigInteger(64)
val half = BigDecimal.parseString("0.5", 10)
val halfSqrtTwo = sqrt(2.0) / 2.0
