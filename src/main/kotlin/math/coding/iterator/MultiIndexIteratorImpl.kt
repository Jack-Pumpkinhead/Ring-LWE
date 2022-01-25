package math.coding.iterator

/**
 * Created by CowardlyLion at 2022/1/25 12:40
 */
class MultiIndexIteratorImpl(bounds: List<UInt>, indexBound: UInt) :MultiIndexIterator<List<UInt>>(bounds, indexBound) {
    override fun returnCode(): List<UInt> = code
}