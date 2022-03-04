package math.abstract_structure.instance

import math.abstract_structure.Field

/**
 * Created by CowardlyLion at 2022/1/25 17:46
 */
object FieldFloat : Field<Float> {

    override val zero = 0F
    override val one = 1F
    override val descriptions: MutableSet<String> = mutableSetOf("field of Float")
    override fun add(x: Float, y: Float): Float = x + y
    override fun negate(a: Float): Float = -a
    override fun multiply(x: Float, y: Float): Float = x * y
    override fun inverse(a: Float): Float = 1.0F / a

    //have rounding errors
    override fun ofInteger(a: UInt): Float = a.toFloat()
    override fun ofInteger(a: Int): Float = a.toFloat()
    override fun ofInteger(a: ULong): Float = a.toFloat()
    override fun ofInteger(a: Long): Float = a.toFloat()

    override val isExactComputation: Boolean get() = false
}