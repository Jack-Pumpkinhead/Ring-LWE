package math.martix

/**
 * Created by CowardlyLion at 2022/1/17 12:01
 */
interface VectorLike<A> {   //TODO it's possible to directly implement List<A>

    val vectorSize: UInt
    fun vectorElementAt(index: UInt): A
    fun vectorElementAtUnsafe(index: UInt): A

}