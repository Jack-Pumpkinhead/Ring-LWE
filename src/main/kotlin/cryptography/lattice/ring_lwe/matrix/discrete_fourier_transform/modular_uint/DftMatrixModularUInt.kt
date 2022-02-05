package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import cryptography.lattice.ring_lwe.coding.permCLInv
import cryptography.lattice.ring_lwe.coding.permLRInv
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrix
import cryptography.lattice.ring_lwe.ring.RootDataUInt
import math.abstract_structure.instance.FieldModularUInt
import math.integer.modular.ModularUInt
import math.martix.AbstractSquareMatrix
import math.martix.formalProduct
import math.martix.permutationMatrix
import math.martix.scalarMatrix
import math.martix.tensor.SquareFormalKroneckerProduct
import math.martix.tensor.SquareFormalProduct
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/2/5 12:10
 */
class DftMatrixModularUInt(root: RootDataUInt<ModularUInt>) : DftMatrix<ModularUInt>(root) {

    init {
        lazyAssert2 {
            assert(root.ring is FieldModularUInt)
        }
    }

    override val underlyingMatrix =
        if (root.order.factors.size == 1) {
            val root1 = root.toPrimePower()
            if (root1.order.power == 1u) {
                DftMatrixPrimeModularUInt(root1.toPrime())
            } else {
                DftMatrixPrimePowerModularUInt(root1)
            }
        } else {
            val factors = root.order.factors.map { it.value }
            SquareFormalProduct(
                ring, listOf(
                    ring.permutationMatrix(permCLInv(factors)),
                    SquareFormalKroneckerProduct(ring, root.allMaximalPrimePowerSubroot().map { root1 ->
                        if (root1.order.power == 1u) {
                            DftMatrixPrimeModularUInt(root1.toPrime())
                        } else {
                            DftMatrixPrimePowerModularUInt(root1)
                        }
                    }),
                    ring.permutationMatrix(permLRInv(factors))
                )
            )
        }

    override fun inverse(): AbstractSquareMatrix<ModularUInt> {
        return ring.formalProduct(DftMatrixModularUInt(root.conjugate()), ring.scalarMatrix(size, ring.inverse(ring.ofInteger(size))))
    }

}