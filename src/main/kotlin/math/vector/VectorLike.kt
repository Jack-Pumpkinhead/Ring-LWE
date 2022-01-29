package math.vector

/**
 * Created by CowardlyLion at 2022/1/17 12:01
 */
interface VectorLike<A> {   //TODO it's possible to directly implement List<A>

    val size: UInt
    fun vectorElementAt(index: UInt): A {
        require(index < size)
        return vectorElementAtUnsafe(index)
    }
    fun vectorElementAtUnsafe(index: UInt): A

    operator fun get(index: UInt) = vectorElementAtUnsafe(index)

}