package math.coding.iterator

import util.stdlib.lazyAssert

/**
 * Created by CowardlyLion at 2022/1/24 20:07
 */
abstract class MultiIndexIteratorLadder<A>(val bounds: List<UInt>, val indexBound: UInt) : Iterator<A> {

    init {
        require(bounds.isNotEmpty())
        lazyAssert { bounds.all { it > 0u } }
    }

    var index = 0u
    val code = MutableList(bounds.size) { 0u }

    private fun isAtEnd(i: Int): Boolean = code[i] == bounds[i] - 1u

    override fun hasNext(): Boolean = index != indexBound

    open fun atIncrease(i: Int) {}
    open fun atCyclicIncreaseToZero(i: Int) {}
    abstract fun returnCode(): A

    override fun next(): A {
        if (index != 0u) {
            var i = bounds.size - 1
            var end = isAtEnd(i)
            while (end) {
                code[i] = 0u
                atCyclicIncreaseToZero(i)
                i--
                end = isAtEnd(i)
            }
            code[i]++
            atIncrease(i)
        }
        index++     //TODO cause first index-code pair not meet.
        return returnCode()
    }

}