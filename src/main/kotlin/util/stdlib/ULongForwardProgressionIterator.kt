package util.stdlib

/**
 * Created by CowardlyLion at 2022/1/17 23:52
 *
 * require [last] - [first] mod [step] == 0
 * require [step] > 0
 */
class ULongForwardProgressionIterator(val first: ULong, val last: ULong, val step: ULong) : Iterator<ULong> {

    var hasNext = first <= last
    var next = if (hasNext) first else last

    override fun hasNext(): Boolean = hasNext

    override fun next(): ULong {
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