package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete

import cryptography.lattice.ring_lwe.coding.LadderSwitcher
import cryptography.lattice.ring_lwe.matrix.TwiddleMatrix
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPrimePower
import cryptography.lattice.ring_lwe.ring.RootDataUIntPrimePower
import math.abstract_structure.instance.FieldComplexNumberDouble
import math.complex_number.ComplexNumber
import math.martix.*
import math.martix.tensor.SquareFormalProduct
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/2/4 16:33
 */
class DftMatrixPrimePowerComplexDouble(root: RootDataUIntPrimePower<ComplexNumber<Double>>, primeCase: DftMatrixPrimeComplexDouble = DftMatrixPrimeComplexDouble(root.primeSubroot())) : DftMatrixPrimePower<ComplexNumber<Double>>(root, primeCase) {

    init {
        lazyAssert2 {
            assert(root.ring == FieldComplexNumberDouble)
        }
    }

    override val underlyingMatrix =
        if (root.order.power == 1u) primeCase else {
            val subRoot = root.subRootData()
            SquareFormalProduct(
                ring, listOf(
                    ring.permutationMatrix(LadderSwitcher(subRoot.order.prime, subRoot.order.value)),
                    ring.whiskered(subRoot.order.prime, DftMatrixPrimePowerComplexDouble(subRoot, primeCase), 1u),
                    TwiddleMatrix(ring, subRoot.order.prime, subRoot.order.value, root.root),
                    ring.whiskered(1u, primeCase, subRoot.order.value)
                )
            )
        }

    override fun inverse(): AbstractSquareMatrix<ComplexNumber<Double>> {
        return ring.formalProduct(DftMatrixPrimePowerComplexDouble(root.conjugate()), ring.scalarMatrix(size, ring.inverse(ring.ofInteger(size))))
    }

}