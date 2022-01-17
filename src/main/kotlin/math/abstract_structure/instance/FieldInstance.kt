package math.abstract_structure.instance

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import math.abstract_structure.Field

/**
 * Created by CowardlyLion at 2022/1/9 13:00
 */

/**
 * behave roughly like a field under 1.79769313486231570e+308
 * */

val fieldFloat: Field<Float> = object : Field<Float> {
    override val zero = 0F
    override val one = 1F
    override val descriptions: MutableSet<String> = mutableSetOf("field of Float")
    override fun add(x: Float, y: Float): Float = x + y
    override fun negate(a: Float): Float = -a
    override fun multiply(x: Float, y: Float): Float = x * y
    override fun inverse(a: Float): Float = 1.0F / a
}

val fieldDouble: Field<Double> = object : Field<Double> {
    override val zero = 0.0
    override val one = 1.0
    override val descriptions: MutableSet<String> = mutableSetOf("field of Double")
    override fun add(x: Double, y: Double): Double = x + y
    override fun negate(a: Double): Double = -a
    override fun multiply(x: Double, y: Double): Double = x * y
    override fun inverse(a: Double): Double = 1.0 / a
}

val fieldBigDecimal: Field<BigDecimal> = object : Field<BigDecimal> {
    override val zero = BigDecimal.ZERO
    override val one = BigDecimal.ONE
    override val descriptions: MutableSet<String> = mutableSetOf("field of BigDecimal")
    override fun add(x: BigDecimal, y: BigDecimal): BigDecimal = x + y
    override fun negate(a: BigDecimal): BigDecimal = -a
    override fun multiply(x: BigDecimal, y: BigDecimal): BigDecimal = x * y
    override fun inverse(a: BigDecimal): BigDecimal = BigDecimal.ONE / a
}

