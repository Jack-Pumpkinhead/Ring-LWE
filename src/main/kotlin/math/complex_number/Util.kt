package math.complex_number

import math.abstract_structure.Ring
import math.abstract_structure.instance.FieldDouble
import math.martix.AbstractColumnVector
import math.martix.columnVector
import math.pi2
import math.roundingErrorDouble
import kotlin.math.*

/**
 * Created by CowardlyLion at 2022/1/17 17:43
 */

/**
 * e^iθ = cos(θ) + i sin(θ)
 */
fun expI(theta: Double): ComplexNumber<Double> = FieldDouble.complexNumber(cos(theta), sin(theta))

/**
 * represent a complex number e^(2πi*(power/order))
 *
 * require [power] and [order] coprime, [power] < [order], [order] > 0
 */
fun cyclotomicNumberUnsafe(power: UInt, order: UInt) =
    when (order) {
        1u   -> FieldComplexNumberDouble.one
        2u   -> FieldDouble.realComplexNumber(FieldDouble.negate(FieldDouble.one))
        4u   -> if (power == 1u) {
            FieldDouble.imaginaryComplexNumber(FieldDouble.one)
        } else {
            FieldDouble.imaginaryComplexNumber(FieldDouble.negate(FieldDouble.one))
        }
        else -> expI((pi2 * power.toDouble()) / order.toDouble())
    }

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
    maxRoundingError = max(maxRoundingError, roundingError)
//    println("err: $roundingError")
    require(roundingError < roundingErrorDouble)
    return roundToInt
}

fun ComplexNumber<Double>.roundToReal(): Double {
    require(imaginary.absoluteValue < roundingErrorDouble) { this.toString() }
    maxRoundingError = max(maxRoundingError, imaginary.absoluteValue)
    return real
}

fun AbstractColumnVector<ComplexNumber<Double>>.roundToReal(): AbstractColumnVector<Double> = FieldDouble.columnVector(size) { i -> this.vectorElementAtUnsafe(i).roundToReal() }

var maxRoundingError: Double = 0.0

fun ComplexNumber<Double>.roundToLong(): Long {
//    println("im: $imaginary")
    require(imaginary.absoluteValue < roundingErrorDouble) { "im: $imaginary" }

    val roundToLong = real.roundToLong()
    if (roundToLong == Long.MIN_VALUE || roundToLong == Long.MAX_VALUE) {
        println("warn: rounding overflow, $real round to $roundToLong")
    }
    val roundingError = real - roundToLong
    maxRoundingError = max(maxRoundingError, roundingError)
//    println("err: $roundingError")

    require(roundingError.absoluteValue < roundingErrorDouble) { "err: $roundingError" }
    return roundToLong
}
