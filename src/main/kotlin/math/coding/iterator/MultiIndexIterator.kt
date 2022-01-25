package math.coding.iterator

import util.stdlib.lazyAssert

/**
 * Created by CowardlyLion at 2022/1/24 20:07
 */
class MultiIndexIterator(val radices: List<UInt>) : Iterator<List<UInt>> {

    init {
        require(radices.isNotEmpty())
        lazyAssert { radices.all { it > 0u } }
    }

    var first = true
    val code = MutableList(radices.size) { 0u }

    private fun isAtEnd(i: Int): Boolean = code[i] == radices[i] - 1u

    override fun hasNext(): Boolean = first || !radices.indices.all { isAtEnd(it) }

    override fun next(): List<UInt> {
        if (first) {
            first = false
        } else {
            var i = radices.size - 1
            var end = isAtEnd(i)
            while (end) {
                code[i] = 0u
                i--
                end = isAtEnd(i)
            }
            code[i]++
        }
        return code
    }

}