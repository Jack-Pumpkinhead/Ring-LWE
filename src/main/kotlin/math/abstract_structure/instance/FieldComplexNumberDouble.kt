package math.abstract_structure.instance

import math.complex_number.ComplexNumber
import math.complex_number.complexNumber
import math.pi2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by CowardlyLion at 2022/1/26 14:37
 */
object FieldComplexNumberDouble : FieldComplexNumber<Double>(FieldDouble) {

    /**
     * e^iθ = cos(θ) + i sin(θ)
     */
    fun expI(theta: Double): ComplexNumber<Double> = FieldDouble.complexNumber(cos(theta), sin(theta))

    /**
     * return e^(i 2π (1/[order]))
     */
    fun primitiveRootOfUnity(order: UInt): ComplexNumber<Double> = expI(pi2 / order.toDouble())


}