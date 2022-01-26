package cryptography.lattice.ring_lwe.matrix

import math.integer.operation.modTimes
import math.martix.AbstractSquareMatrix
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/26 16:26
 */
class ChineseRemainderTransformMatrixPrime<A>(val root: RootDataUIntPrime<A>) : AbstractSquareMatrix<A>(root.ring, root.order - 1u) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        return ring.powerM(root.root, modTimes(row + 1u, column, root.order))
    }

}