package math.complex_number

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPI
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPPI
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.ProperPrimePowerProductDftMatrix
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double.DftMatrixPPIBuilderComplexDouble
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double.PrimeDftMatrixComplexDouble
import cryptography.lattice.ring_lwe.ring.RootUIntP
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import math.abstract_structure.instance.FieldDouble
import math.integer.uint.factored.*
import math.pi2

/**
 * Created by CowardlyLion at 2022/1/26 14:37
 */
object FieldComplexNumberDouble : FieldComplexNumber<Double>(FieldDouble) {


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