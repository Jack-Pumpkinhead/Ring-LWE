package cryptography.lattice.ring_lwe.matrix

import math.integer.coprimeElements
import math.integer.operation.modTimes
import math.martix.AbstractSquareMatrix
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/20 14:53
 */
class ChineseRemainderTransformMatrix<A>(val root: RootDataUInt<A>) : AbstractSquareMatrix<A>(root.ring, root.eulerTotient()) {

    val coprimeElements: List<UInt> = root.order.coprimeElements().map { it }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        return ring.powerM(root.root, modTimes(coprimeElements[row.toInt()], column, root.order))
    }


}