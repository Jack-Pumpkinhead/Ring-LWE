package math.abstract_structure.instance

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import math.abstract_structure.Field

/**
 * Created by CowardlyLion at 2022/1/25 17:50
 */
object FieldBigDecimal : Field<BigDecimal> {
    override val zero = BigDecimal.ZERO
    override val one = BigDecimal.ONE
    override val descriptions: MutableSet<String> = mutableSetOf("field of BigDecimal")
    override fun add(x: BigDecimal, y: BigDecimal): BigDecimal = x + y
    override fun negate(a: BigDecimal): BigDecimal = -a
    override fun multiply(x: BigDecimal, y: BigDecimal): BigDecimal = x * y
    override fun inverse(a: BigDecimal): BigDecimal = BigDecimal.ONE / a

    override fun ofInteger(a: UInt): BigDecimal = BigDecimal.fromUInt(a)
    override fun ofInteger(a: Int): BigDecimal = BigDecimal.fromInt(a)

}