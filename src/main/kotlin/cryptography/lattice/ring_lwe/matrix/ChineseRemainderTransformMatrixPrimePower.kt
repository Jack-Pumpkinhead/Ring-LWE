package cryptography.lattice.ring_lwe.matrix

import math.abstract_structure.Ring
import math.integer.operation.modTimes
import math.martix.AbstractSquareMatrix
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/26 16:20
 */
class ChineseRemainderTransformMatrixPrimePower<A>(val root: RootDataUIntPrimePower<A>, primeCase: ChineseRemainderTransformMatrixPrime<A>) : AbstractSquareMatrix<A> {

    override val ring: Ring<A> get() = root.ring

    override val size: UInt get() = root.order.eulerTotient

    fun coprimeAt(i: UInt) = (i / (root.order.prime - 1u)) + i + 1u

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        return ring.powerM(root.root, modTimes(coprimeAt(row), column, root.order.value))
    }

    override fun determinant(): A {
        TODO("Not yet implemented")
    }

    override fun inverse(): AbstractSquareMatrix<A> {
        TODO("Not yet implemented")
    }

}