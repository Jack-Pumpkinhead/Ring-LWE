package util.stdlib

/**
 * Created by CowardlyLion at 2022/1/28 22:34
 */
class UIntForwardProgression(start: UInt, endInclusive: UInt, val step: UInt) : Iterable<UInt> {

    val first: UInt = start
    val last: UInt

    init {
        require(step != 0u)
        last = (if (endInclusive < start) endInclusive else endInclusive - (endInclusive - start).mod(step))
    }

    override fun iterator(): Iterator<UInt> = UIntForwardProgressionIterator(first, last, step)

    fun isEmpty() = last < first

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UIntForwardProgression) return false

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