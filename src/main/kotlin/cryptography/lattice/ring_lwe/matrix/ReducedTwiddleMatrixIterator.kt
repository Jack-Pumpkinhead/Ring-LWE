package cryptography.lattice.ring_lwe.matrix

import math.coding.LadderIndex
import math.coding.iterator.MultiIndexIteratorLadder

/**
 * Created by CowardlyLion at 2022/2/23 15:38
 */
class ReducedTwiddleMatrixIterator(ladderIndex: LadderIndex) : MultiIndexIteratorLadder<IndexAndPower>(ladderIndex.bounds, ladderIndex.indexBound) {

    var power = 0u

    override fun returnCode(): IndexAndPower = IndexAndPower(index - 1u, power)

    override fun atIncrease(i: Int) {
        if (i == 1) {
            power += (code[0] + 1u)
        }
    }

    override fun atCyclicIncreaseToZero(i: Int) {
        power = 0u
    }
}