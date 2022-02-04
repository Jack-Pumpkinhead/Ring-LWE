package math.abstract_structure.instance

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPrimePower
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete.DftMatrixComplexDouble
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete.DftMatrixPrimeComplexDouble
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete.DftMatrixPrimePowerComplexDouble
import cryptography.lattice.ring_lwe.ring.RootDataUInt
import cryptography.lattice.ring_lwe.ring.RootDataUIntPrime
import cryptography.lattice.ring_lwe.ring.RootDataUIntPrimePower
import math.complex_number.ComplexNumber
import math.complex_number.complexNumber
import math.integer.FactorizationUInt
import math.integer.FactorizationUIntPrime
import math.integer.FactorizationUIntPrimePower
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
    fun root(order: FactorizationUInt): RootDataUInt<ComplexNumber<Double>> {
        val root = expI(pi2 / order.value.toDouble())
        return RootDataUInt(this, root, order)
    }

    fun root(order: FactorizationUIntPrimePower): RootDataUIntPrimePower<ComplexNumber<Double>> {
        val root = expI(pi2 / order.value.toDouble())
        return RootDataUIntPrimePower(this, root, order)
    }

    fun root(order: FactorizationUIntPrime): RootDataUIntPrime<ComplexNumber<Double>> {
        val root = expI(pi2 / order.value.toDouble())
        return RootDataUIntPrime(this, root, order)
    }

    fun dft(order: FactorizationUInt) = DftMatrixComplexDouble(root(order))

    fun dft(order: FactorizationUIntPrimePower): DftMatrixPrimePower<ComplexNumber<Double>> {
        val root = root(order)
        return DftMatrixPrimePowerComplexDouble(root, DftMatrixPrimeComplexDouble(root.primeSubroot()))
    }

    fun dft(order: FactorizationUIntPrime) = DftMatrixPrimeComplexDouble(root(order))


}