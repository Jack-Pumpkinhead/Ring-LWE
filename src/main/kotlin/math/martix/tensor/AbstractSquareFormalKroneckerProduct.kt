package math.martix.tensor

import math.coding.LadderIndex
import math.martix.AbstractSquareMatrix

/**
 * Created by CowardlyLion at 2022/2/13 14:29
 */
interface AbstractSquareFormalKroneckerProduct<A> : AbstractSquareMatrix<A>, AbstractFormalKroneckerProduct<A> {

    override val elements: List<AbstractSquareMatrix<A>>

    val index: LadderIndex

    override val rowIndex: LadderIndex
        get() = index
    override val columnIndex: LadderIndex
        get() = index

    override fun hasInverse(): Boolean = elements.all { it.hasInverse() }

}