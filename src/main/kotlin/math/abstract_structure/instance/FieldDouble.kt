package math.abstract_structure.instance

import math.abstract_structure.Field

/**
 * Created by CowardlyLion at 2022/1/25 17:48
 *
 * behave roughly like a field under 1.79769313486231570e+308
 */
object FieldDouble : Field<Double> {
    override val zero = 0.0
    override val one = 1.0
    override val descriptions: MutableSet<String> = mutableSetOf("field of Double")
    override fun add(x: Double, y: Double): Double = x + y
    override fun negate(a: Double): Double = -a
    override fun multiply(x: Double, y: Double): Double = x * y
    override fun inverse(a: Double): Double = 1.0 / a

    override fun ofInteger(a: UInt): Double = a.toDouble()
    override fun ofInteger(a: Int): Double = a.toDouble()
}