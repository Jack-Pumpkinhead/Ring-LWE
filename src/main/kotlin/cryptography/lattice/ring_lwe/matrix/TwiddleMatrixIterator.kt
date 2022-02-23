package cryptography.lattice.ring_lwe.matrix

import math.coding.LadderIndex
import math.coding.iterator.MultiIndexIteratorLadder

/**
 * Created by CowardlyLion at 2022/2/23 15:02
 */
class TwiddleMatrixIterator(ladderIndex: LadderIndex) : MultiIndexIteratorLadder<IndexAndPower>(ladderIndex.bounds, ladderIndex.indexBound) {

    var power = 0u

    override fun returnCode(): IndexAndPower = IndexAndPower(index - 1u, power)

    override fun atIncrease(i: Int) {
        if (i == 1) {
            power += code[0]
        }
    }

    override fun atCyclicIncreaseToZero(i: Int) {
        power = 0u
    }
}