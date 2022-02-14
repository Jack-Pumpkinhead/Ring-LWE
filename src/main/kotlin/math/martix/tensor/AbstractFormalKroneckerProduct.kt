package math.martix.tensor

import math.coding.LadderIndex
import math.martix.AbstractMatrix
import math.operation.product

/**
 * Created by CowardlyLion at 2022/2/13 14:30
 */
interface AbstractFormalKroneckerProduct<A> : AbstractMatrix<A> {

    val elements: List<AbstractMatrix<A>>

    val rowIndex: LadderIndex

    val columnIndex: LadderIndex

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        val row1 = rowIndex.decode(row)
        val column1 = columnIndex.decode(column)
        return ring.product(0u until elements.size.toUInt()) { i -> elements[i.toInt()].elementAtUnsafe(row1[i.toInt()], column1[i.toInt()]) }
    }

    override fun downCast(): AbstractMatrix<A> = when (elements.size) {
        1    -> elements[0]
        else -> this
    }

}