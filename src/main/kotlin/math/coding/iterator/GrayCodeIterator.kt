package math.coding.iterator

import util.stdlib.lazyAssert

/**
 * Created by CowardlyLion at 2022/1/24 17:16
 */
class GrayCodeIterator(val radices: List<UInt>) : Iterator<List<UInt>> {

    init {
        require(radices.isNotEmpty())
        lazyAssert { radices.all { it > 0u } }
    }

    var first = true
    val code = MutableList(radices.size) { 0u }
    val reverseOrder = MutableList(radices.size) { false }
    var i = radices.size - 1

    private fun isAtEnd(i: Int): Boolean = if (reverseOrder[i]) code[i] == 0u else code[i] == radices[i] - 1u

    override fun hasNext(): Boolean = first || i != radices.size - 1 || !(0..i).all { isAtEnd(it) }

    override fun next(): List<UInt> {
        if (first) {
            first = false
        } else {
            var shouldTryNext = tryNext()
            while (shouldTryNext) {
                shouldTryNext = tryNext()
            }
        }
        return code
    }

    private fun tryNext(): Boolean =
        if (reverseOrder[i]) {
            if (code[i] == 0u) {
                reverseOrder[i] = !reverseOrder[i]
                i--
                true
            } else {
                code[i]--
                i = radices.size - 1
                false
            }
        } else {
            if (code[i] == radices[i] - 1u) {
                reverseOrder[i] = !reverseOrder[i]
                i--
                true
            } else {
                code[i]++
                i = radices.size - 1
                false
            }
        }

}