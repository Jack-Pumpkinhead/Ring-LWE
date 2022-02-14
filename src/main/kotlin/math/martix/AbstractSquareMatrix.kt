package math.martix

/**
 * Created by CowardlyLion at 2022/1/20 15:00
 */
interface AbstractSquareMatrix<A> : AbstractMatrix<A> {

    val size: UInt

    override val rows: UInt get() = size
    override val columns: UInt get() = size

    override fun isSquareMatrix(): Boolean = true

    fun determinant(): A

    fun hasInverse(): Boolean = ring.hasInverse(determinant())

    val inverse: AbstractSquareMatrix<A>

}