package math.coding

import math.abstract_structure.instance.ringUInt
import math.operations.innerProduct
import math.runningFoldRight

/**
 * Created by CowardlyLion at 2022/1/9 22:17
 *
 * give a bijection between (multi-index {a_i}_i with bound {b_i}_i) to (0 until Π_i b_i)
 */
class BoundedMultiIndex(bound: List<UInt>) {

    val indexBase = bound.runningFoldRight(1u) { acc, b -> acc * b }.drop(1)

    fun toMultiIndex(index: UInt): List<UInt> {
        val indices = mutableListOf<UInt>()
        var n = index
        for (base in indexBase) {
            indices += n / base
            n %= base
        }
        return indices
    }

    fun toIndex(indices: List<UInt>): UInt = ringUInt.innerProduct(indexBase, indices)


}