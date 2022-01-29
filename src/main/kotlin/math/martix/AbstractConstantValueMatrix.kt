package math.martix

/**
 * Created by CowardlyLion at 2022/1/29 21:04
 */
interface AbstractConstantValueMatrix<A> : AbstractMatrix<A> {

    val value: A

    override fun elementAtUnsafe(row: UInt, column: UInt): A = value

}