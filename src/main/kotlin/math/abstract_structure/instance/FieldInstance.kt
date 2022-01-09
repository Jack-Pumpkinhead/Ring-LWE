package math.abstract_structure.instance

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import math.abstract_structure.Field

/**
 * Created by CowardlyLion at 2022/1/9 13:00
 */

/**
 * behave roughly like a field under 1.79769313486231570e+308
 * */

val fieldFloat: Field<Float> = object : Field<Float>(mutableSetOf("field of Float"), 0.0F, 1.0F) {
    override fun add(x: Float, y: Float): Float = x + y
    override fun negate(a: Float): Float = -a
    override fun multiply(x: Float, y: Float): Float = x * y
    override fun inverseImpl(a: Float): Float = 1.0F / a
}

val fieldDouble: Field<Double> = object : Field<Double>(mutableSetOf("field of Double"), 0.0, 1.0) {
    override fun add(x: Double, y: Double): Double = x + y
    override fun negate(a: Double): Double = -a
    override fun multiply(x: Double, y: Double): Double = x * y
    override fun inverseImpl(a: Double): Double = 1.0 / a
}

val fieldBigDecimal: Field<BigDecimal> = object : Field<BigDecimal>(mutableSetOf("field of BigDecimal"), BigDecimal.ZERO, BigDecimal.ONE) {
    override fun add(x: BigDecimal, y: BigDecimal): BigDecimal = x + y
    override fun negate(a: BigDecimal): BigDecimal = -a
    override fun multiply(x: BigDecimal, y: BigDecimal): BigDecimal = x * y
    override fun inverseImpl(a: BigDecimal): BigDecimal = BigDecimal.ONE / a
}

