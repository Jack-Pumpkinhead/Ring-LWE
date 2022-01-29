package util.stdlib

/**
 * Created by CowardlyLion at 2022/1/28 22:35
 *
 * require [last] - [first] mod [step] == 0
 *
 * require [step] > 0
 */
class UIntForwardProgressionIterator(val first: UInt, val last: UInt, val step: UInt) : Iterator<UInt> {

    var hasNext = first <= last
    var next = if (hasNext) first else last

    override fun hasNext(): Boolean = hasNext

    override fun next(): UInt {
        val value = next
        if (value == last) {
            if (!hasNext) throw NoSuchElementException()
            hasNext = false
        } else {
            next += step
        }
        return value
    }

}