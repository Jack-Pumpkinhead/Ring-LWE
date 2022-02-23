package math.coding.iterator

/**
 * Created by CowardlyLion at 2022/1/25 12:40
 */
class MultiIndexIteratorLadderImpl(bounds: List<UInt>, indexBound: UInt) :MultiIndexIteratorLadder<List<UInt>>(bounds, indexBound) {
    override fun returnCode(): List<UInt> = code
}