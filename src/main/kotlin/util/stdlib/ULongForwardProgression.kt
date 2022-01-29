package util.stdlib

/**
 * Created by CowardlyLion at 2022/1/17 23:07
 */
class ULongForwardProgression(start: ULong, endInclusive: ULong, val step: ULong) : Iterable<ULong> {

    val first: ULong = start
    val last: ULong

    init {
        require(step != 0uL)
        last = (if (endInclusive < start) endInclusive else endInclusive - (endInclusive - start).mod(step))
    }

    override fun iterator(): Iterator<ULong> = ULongForwardProgressionIterator(first, last, step)

    fun isEmpty() = last < first

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ULongForwardProgression) return false

        if (this.isEmpty() && other.isEmpty()) return true

        if (step != other.step) return false
        if (first != other.first) return false
        if (last != other.last) return false

        return true
    }

    override fun hashCode(): Int {
        if (isEmpty()) return -1
        var result = step.hashCode()
        result = 31 * result + first.hashCode()
        result = 31 * result + last.hashCode()
        return result
    }

}