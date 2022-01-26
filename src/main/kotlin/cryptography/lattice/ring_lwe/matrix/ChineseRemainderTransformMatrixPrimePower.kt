package cryptography.lattice.ring_lwe.matrix

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.RootDataULongPrimePower
import math.integer.operation.modTimes
import math.martix.AbstractSquareMatrix
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/26 16:20
 */
class ChineseRemainderTransformMatrixPrimePower<A>(val root: RootDataULongPrimePower<A>, primeCase: ChineseRemainderTransformMatrixPrime<A>) : AbstractSquareMatrix<A>(root.ring, root.eulerTotient().toUInt()) {

    fun coprimeAt(i: UInt) = (i / (root.order.prime.toUInt() - 1u)) + i + 1u

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        return ring.powerM(root.root, modTimes(coprimeAt(row), column, root.order.primePower.toUInt()))
    }

}