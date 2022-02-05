package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import cryptography.lattice.ring_lwe.coding.LadderSwitcher
import cryptography.lattice.ring_lwe.matrix.TwiddleMatrix
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPrimePower
import cryptography.lattice.ring_lwe.ring.RootDataUIntPrimePower
import math.abstract_structure.instance.FieldModularUInt
import math.integer.modular.ModularUInt
import math.martix.*
import math.martix.tensor.SquareFormalProduct
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/2/5 12:12
 */
class DftMatrixPrimePowerModularUInt(root: RootDataUIntPrimePower<ModularUInt>, primeCase: DftMatrixPrimeModularUInt = DftMatrixPrimeModularUInt(root.primeSubroot())) : DftMatrixPrimePower<ModularUInt>(root, primeCase) {

    init {
        lazyAssert2 {
            assert(root.ring is FieldModularUInt)
        }
    }

    override val underlyingMatrix =
        if (root.order.power == 1u) primeCase else {
            val subRoot = root.subRootData()
            SquareFormalProduct(
                ring, listOf(
                    ring.permutationMatrix(LadderSwitcher(subRoot.order.prime, subRoot.order.value)),
                    ring.whiskered(subRoot.order.prime, DftMatrixPrimePowerModularUInt(subRoot, primeCase), 1u),
                    TwiddleMatrix(ring, subRoot.order.prime, subRoot.order.value, root.root),
                    ring.whiskered(1u, primeCase, subRoot.order.value)
                )
            )
        }

    override fun inverse(): AbstractSquareMatrix<ModularUInt> {
        return ring.formalProduct(DftMatrixPrimePowerModularUInt(root.conjugate()), ring.scalarMatrix(size, ring.inverse(ring.ofInteger(size))))
    }


}