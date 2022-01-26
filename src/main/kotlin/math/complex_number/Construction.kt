package math.complex_number

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/17 17:43
 */

fun <A> Ring<A>.complexNumber(real: A, imaginary: A) = ComplexNumber(this, real, imaginary)

