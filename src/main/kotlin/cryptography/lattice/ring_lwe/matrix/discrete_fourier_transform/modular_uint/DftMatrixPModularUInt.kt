package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixP
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.RowSummationMatrix
import cryptography.lattice.ring_lwe.ring.RootP
import math.abstract_structure.Ring
import math.integer.uint.modTimes
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.martix.*
import math.martix.partition.SquareRowBipartiteMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/2/5 12:13
 */
class DftMatrixPModularUInt(override val root: RootP<ModularUInt>) : DftMatrixP<ModularUInt>, BackingSquareMatrix<ModularUInt> {

    init {
        lazyAssert2 {
            assert(root.ring is FieldModularUInt)
        }
    }

    override val size: UInt get() = root.order.value

    override val ring: Ring<ModularUInt> get() = root.ring

    //TODO scale threshold jointly with PP,PPP case, or leave it as is assuming PP,PPP cases always faster to factorize.
    override val underlyingMatrix =
        if (size <= 739u) {
            ring.squareMatrix(size) { i, j ->
                root.root.cachedPower(modTimes(i, j, size))
            }
        } else {
            SquareRowBipartiteMatrix(RowSummationMatrix(ring, size), DftMatrixPrimeLowerPartModularUInt(root))
        }

    override fun hasInverse(): Boolean = ring.hasInverse(ring.ofInteger(size))

    override val inverse: AbstractSquareMatrix<ModularUInt> by lazy {
        ring.formalProduct(DftMatrixBuilderModularUInt.compute(root.inverse), ring.scalarMatrix(size, ring.inverse(ring.ofInteger(size))))
    }

}