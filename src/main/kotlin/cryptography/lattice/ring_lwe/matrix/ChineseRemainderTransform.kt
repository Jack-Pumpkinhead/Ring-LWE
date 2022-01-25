package cryptography.lattice.ring_lwe.matrix

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.RootData
import kotlinx.coroutines.runBlocking
import math.integer.coprimeElements
import math.integer.eulerTotient
import math.integer.operation.modTimes
import math.martix.AbstractSquareMatrix
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/20 14:53
 */
class ChineseRemainderTransform<A>(val root: RootData<A>) : AbstractSquareMatrix<A>(root.ring, runBlocking { root.order.eulerTotient().toUInt() }) {

    val coprimeElements: List<UInt> = root.order.coprimeElements().map { it.toUInt() }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        return ring.powerM(root.root, modTimes(coprimeElements[row.toInt()], column, root.order.toUInt()))
    }


}