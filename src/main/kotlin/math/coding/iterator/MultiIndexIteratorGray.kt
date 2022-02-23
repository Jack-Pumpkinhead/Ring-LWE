package math.coding.iterator

import util.stdlib.lazyAssert

/**
 * Created by CowardlyLion at 2022/1/24 17:16
 */
abstract class MultiIndexIteratorGray<A>(val bounds: List<UInt>, val indexBound: UInt) : Iterator<A> {

    init {
        require(bounds.isNotEmpty())
        lazyAssert { bounds.all { it > 0u } }
    }

    var index = 0u
    val code = MutableList(bounds.size) { 0u }

    private val reverseOrder = MutableList(bounds.size) { false }
    private var i = bounds.size - 1

    private fun isAtEnd(i: Int): Boolean = if (reverseOrder[i]) code[i] == 0u else code[i] == bounds[i] - 1u

    override fun hasNext(): Boolean = index != indexBound

    /**
     * return [code] represented by [index]
     */
    override fun next(): A {
        if (index != 0u) {
            var shouldTryNext = tryNext()
            while (shouldTryNext) {
                shouldTryNext = tryNext()
            }
        }
        index++     //TODO cause first index-code pair not meet.
        return returnCode()
    }

    open fun atIncrease(i: Int) {}
    open fun atDecrease(i: Int) {}
    abstract fun returnCode(): A

    private fun tryNext(): Boolean =
        if (reverseOrder[i]) {
            if (code[i] == 0u) {
                reverseOrder[i] = !reverseOrder[i]
                i--
                true
            } else {
                code[i]--
                atDecrease(i)
                i = bounds.size - 1
                false
            }
        } else {
            if (code[i] == bounds[i] - 1u) {
                reverseOrder[i] = !reverseOrder[i]
                i--
                true
            } else {
                code[i]++
                atIncrease(i)
                i = bounds.size - 1
                false
            }
        }

}