package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPI
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.RowSummationMatrix
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import math.abstract_structure.Ring
import math.abstract_structure.instance.FieldComplexNumberDouble
import math.complex_number.ComplexNumber
import math.integer.uint.modTimes
import math.martix.*
import math.martix.partition.SquareRowBipartiteMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/2/4 16:35
 */
class PrimeDftMatrixComplexDouble(override val root: RootUIntPI<ComplexNumber<Double>>) : DftMatrixPI<ComplexNumber<Double>>, BackingSquareMatrix<ComplexNumber<Double>> {

    init {
        lazyAssert2 {
            assert(root.ring == FieldComplexNumberDouble)
        }
    }

    override val size: UInt get() = root.order.value

    override val ring: Ring<ComplexNumber<Double>> get() = root.ring

    override val underlyingMatrix =
        if (size <= 389u) {
            ring.squareMatrix(size) { i, j ->
                root.cachedPower(modTimes(i, j, size))
            }
        } else {
            SquareRowBipartiteMatrix(RowSummationMatrix(ring, size), DftMatrixPrimeLowerPartComplexDouble(root))
        }

    override fun hasInverse(): Boolean = ring.hasInverse(ring.ofInteger(size))

    override val inverse: AbstractSquareMatrix<ComplexNumber<Double>> by lazy {
        ring.formalProduct(PrimeDftMatrixComplexDouble(root.inverse), ring.scalarMatrix(size, ring.inverse(ring.ofInteger(size))))
    }

}