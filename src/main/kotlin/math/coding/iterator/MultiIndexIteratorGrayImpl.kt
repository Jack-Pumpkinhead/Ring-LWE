package math.coding.iterator

/**
 * Created by CowardlyLion at 2022/1/25 12:36
 */
class MultiIndexIteratorGrayImpl(bounds: List<UInt>, indexBound: UInt) : MultiIndexIteratorGray<List<UInt>>(bounds, indexBound) {
    override fun returnCode(): List<UInt> = code
}