package math.martix

import math.operation.product

/**
 * Created by CowardlyLion at 2022/2/13 14:08
 */
interface AbstractSquareFormalProduct<A> : AbstractFormalProduct<A>, AbstractSquareMatrix<A> {

    override val matrices: List<AbstractSquareMatrix<A>>

    override fun determinant(): A = ring.product(matrices.map { it.determinant() })

    override fun hasInverse(): Boolean = matrices.all { it.hasInverse() }

    //TODO square product should have a better memory-free multiply-to

}