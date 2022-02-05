package math.complex_number

import math.abstract_structure.Ring
import math.roundingErrorDouble
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * Created by CowardlyLion at 2022/1/17 17:43
 */

fun <A> Ring<A>.complexNumber(real: A, imaginary: A) = ComplexNumber(this, real, imaginary)
fun <A> Ring<A>.realComplexNumber(real: A) = RealComplexNumber(this, real)
fun <A> Ring<A>.imaginaryComplexNumber(imaginary: A) = ImaginaryComplexNumber(this, imaginary)

fun ComplexNumber<Double>.roundToInt(): Int {
//    println("im: $imaginary")
    require(imaginary.absoluteValue < roundingErrorDouble)

    val roundToInt = real.roundToInt()
    if (roundToInt == Int.MIN_VALUE || roundToInt == Int.MAX_VALUE) {
        println("warn: rounding overflow, $real round to $roundToInt")
    }
    val roundingError = (real - roundToInt).absoluteValue
//    println("err: $roundingError")
    require(roundingError < roundingErrorDouble)
    return roundToInt
}

fun ComplexNumber<Double>.roundToLong(): Long {
//    println("im: $imaginary")
    require(imaginary.absoluteValue < roundingErrorDouble) { "im: $imaginary" }

    val roundToLong = real.roundToLong()
    if (roundToLong == Long.MIN_VALUE || roundToLong == Long.MAX_VALUE) {
        println("warn: rounding overflow, $real round to $roundToLong")
    }
    val roundingError = real - roundToLong
//    println("err: $roundingError")
    require(roundingError.absoluteValue < roundingErrorDouble) { "err: $roundingError" }
    return roundToLong
}
