package math.martix

/**
 * Created by CowardlyLion at 2022/1/17 12:01
 */
interface VectorLike<A> {

    fun vectorElementAt(index: UInt): A
    fun vectorElementAtUnsafe(index: UInt): A

}