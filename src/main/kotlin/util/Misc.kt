package util

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.integer.BigInteger
import math.integer.uint.modular.ModularUInt
import math.integer.ulong.modular.ModularULong

/**
 * Created by CowardlyLion at 2022/2/11 16:43
 */
fun BigInteger.toBigDecimal(decimalMode: DecimalMode? = null) = BigDecimal.fromBigInteger(this, decimalMode)
fun BigDecimal.ceilToInteger(): BigDecimal = this.roundToDigitPositionAfterDecimalPoint(0L, RoundingMode.CEILING)
fun BigDecimal.ceilToBigInteger(): BigInteger = this.roundToDigitPositionAfterDecimalPoint(0L, RoundingMode.CEILING).toBigInteger()
fun BigDecimal.differenceFromCeilInteger(): BigDecimal = this.roundToDigitPositionAfterDecimalPoint(0L, RoundingMode.CEILING) - this

fun UInt.toUIntModular(modulus: UInt): ModularUInt = ModularUInt(modulus, this.mod(modulus))
fun ULong.toULongModular(modulus: ULong): ModularULong = ModularULong(modulus, this.mod(modulus))