package math.vector

/**
 * Created by CowardlyLion at 2022/1/28 17:06
 */
interface MutableVectorLike<A> : VectorLike<A> {

    fun setVectorElementAt(index: UInt, a: A) {
        require(index < size)
        setVectorElementAtUnsafe(index, a)
    }

    fun setVectorElementAtUnsafe(index: UInt, a: A)

}