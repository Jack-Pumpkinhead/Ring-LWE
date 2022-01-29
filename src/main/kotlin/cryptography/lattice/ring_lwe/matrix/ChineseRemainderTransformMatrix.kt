package cryptography.lattice.ring_lwe.matrix

import cryptography.lattice.ring_lwe.ring.RootDataUInt
import math.abstract_structure.Ring
import math.integer.coprimeElements
import math.integer.operation.modTimes
import math.martix.AbstractSquareMatrix
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/20 14:53
 */
class ChineseRemainderTransformMatrix<A>(val root: RootDataUInt<A>) : AbstractSquareMatrix<A> {

    override val ring: Ring<A> get() = root.ring

    override val size: UInt get() = root.order.eulerTotient

    val coprimeElements: List<UInt> = root.order.value.coprimeElements().map { it }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        return ring.powerM(root.root, modTimes(coprimeElements[row.toInt()], column, root.order.value))
    }

    override fun determinant(): A {
        TODO("Not yet implemented")
    }

    override fun inverse(): AbstractSquareMatrix<A> {
        TODO("Not yet implemented")
    }


}