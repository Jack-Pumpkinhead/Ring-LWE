package cryptography.lattice.ring_lwe.matrix

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.RootDataULong
import math.integer.operation.modTimes
import math.martix.AbstractSquareMatrix
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/26 16:26
 */
class ChineseRemainderTransformMatrixPrime<A>(val root: RootDataULong<A>) : AbstractSquareMatrix<A>(root.ring, root.order.toUInt() - 1u) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        return ring.powerM(root.root, modTimes(row + 1u, column, root.order.toUInt()))
    }

}