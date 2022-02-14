package math.martix

/**
 * Created by CowardlyLion at 2022/1/29 22:33
 *
 * not all methods are delegated
 */
interface BackingSquareMatrix<A> : BackingMatrix<A>, AbstractSquareMatrix<A> {

    override val underlyingMatrix: AbstractSquareMatrix<A>

    override val size: UInt get() = underlyingMatrix.size
    override val rows: UInt get() = size
    override val columns: UInt get() = size

    override fun determinant(): A = underlyingMatrix.determinant()
    override fun hasInverse(): Boolean = underlyingMatrix.hasInverse()
    override val inverse: AbstractSquareMatrix<A> get() = underlyingMatrix.inverse

}