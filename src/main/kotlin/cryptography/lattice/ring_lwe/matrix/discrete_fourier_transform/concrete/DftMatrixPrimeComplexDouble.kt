package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPrime
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.RowSummationMatrix
import cryptography.lattice.ring_lwe.ring.RootDataUIntPrime
import math.abstract_structure.instance.FieldComplexNumberDouble
import math.complex_number.ComplexNumber
import math.integer.operation.modTimes
import math.martix.AbstractSquareMatrix
import math.martix.formalProduct
import math.martix.partition.SquareRowBipartiteMatrix
import math.martix.scalarMatrix
import math.martix.squareMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/2/4 16:35
 */
class DftMatrixPrimeComplexDouble(root: RootDataUIntPrime<ComplexNumber<Double>>) : DftMatrixPrime<ComplexNumber<Double>>(root) {

    init {
        lazyAssert2 {
            assert(root.ring == FieldComplexNumberDouble)
        }
    }

    override val underlyingMatrix =
        if (size <= 3u) {
            ring.squareMatrix(size) { i, j ->
                root.power(modTimes(i, j, size))
            }
        } else {
            SquareRowBipartiteMatrix(RowSummationMatrix(ring, size), DftMatrixPrimeLowerPartComplexDouble(root))
        }

    override fun inverse(): AbstractSquareMatrix<ComplexNumber<Double>> {
        return ring.formalProduct(DftMatrixPrimeComplexDouble(root.conjugate()), ring.scalarMatrix(size, ring.inverse(ring.ofInteger(size))))
    }

}