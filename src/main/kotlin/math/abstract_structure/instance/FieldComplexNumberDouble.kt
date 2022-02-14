package math.abstract_structure.instance

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.*
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double.PrimeDftMatrixComplexDouble
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double.DftMatrixPPIBuilderComplexDouble
import cryptography.lattice.ring_lwe.ring.RootUIntP
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import math.complex_number.ComplexNumber
import math.complex_number.complexNumber
import math.integer.uint.factored.*
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

    /**
     * primitive [order]-th root of unity
     */
    fun root(order: UIntP) = RootUIntP(this, expI(pi2 / order.value.toDouble()), order)
    fun root(order: UIntPP) = RootUIntPP(this, expI(pi2 / order.value.toDouble()), order)
    fun root(order: UIntPPP) = RootUIntPPP(this, expI(pi2 / order.value.toDouble()), order)
    fun root(order: UIntPPI) =
        when (order) {
            is UIntPP -> root(order)
            is UIntP  -> root(order)
            else      -> error("unknown type of order $order, class: ${order::class}")
        }

    fun root(order: UIntPPPI) =
        when (order) {
            is UIntPPP -> root(order)
            is UIntPP  -> root(order)
            is UIntP   -> root(order)
            else       -> error("unknown type of order $order, class: ${order::class}")
        }


    fun dft(order: UIntP) = PrimeDftMatrixComplexDouble(root(order))
    fun dft(order: UIntPPP) = DftMatrixPPP(DftMatrixPPIBuilderComplexDouble, root(order))
    fun dft(order: UIntPPI): DftMatrixPPI<ComplexNumber<Double>> = DftMatrixPPIBuilderComplexDouble.build(root(order))
    fun dft(order: UIntPPPI): DftMatrixPPPI<ComplexNumber<Double>> =
        when (order) {
            is UIntPPP -> ProperPrimePowerProductDftMatrix(DftMatrixPPIBuilderComplexDouble, root(order))
            is UIntPPI -> dft(order)
            else       -> error("unknown type of order $order, class: ${order::class}")
        }

}