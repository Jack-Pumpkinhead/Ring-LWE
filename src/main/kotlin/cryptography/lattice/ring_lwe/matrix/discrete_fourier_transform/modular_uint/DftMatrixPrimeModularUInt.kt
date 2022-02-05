package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPrime
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.RowSummationMatrix
import cryptography.lattice.ring_lwe.ring.RootDataUIntPrime
import math.abstract_structure.instance.FieldModularUInt
import math.integer.modular.ModularUInt
import math.integer.operation.modTimes
import math.martix.AbstractSquareMatrix
import math.martix.formalProduct
import math.martix.partition.SquareRowBipartiteMatrix
import math.martix.scalarMatrix
import math.martix.squareMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/2/5 12:13
 */
class DftMatrixPrimeModularUInt(root: RootDataUIntPrime<ModularUInt>) :DftMatrixPrime<ModularUInt>(root) {

    init {
        lazyAssert2 {
            assert(root.ring is FieldModularUInt)
        }
    }

    override val underlyingMatrix =
        if (size <= 3u) {
            ring.squareMatrix(size) { i, j ->
                root.power(modTimes(i, j, size))
            }
        } else {
            SquareRowBipartiteMatrix(RowSummationMatrix(ring, size), DftMatrixPrimeLowerPartModularUInt(root))
        }

    override fun inverse(): AbstractSquareMatrix<ModularUInt> {
        return ring.formalProduct(DftMatrixPrimeModularUInt(root.conjugate()), ring.scalarMatrix(size, ring.inverse(ring.ofInteger(size))))
    }

}