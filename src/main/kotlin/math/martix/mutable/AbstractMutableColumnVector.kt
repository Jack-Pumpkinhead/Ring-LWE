package math.martix.mutable

import math.martix.AbstractColumnVector
import math.vector.MutableVectorLike

/**
 * Created by CowardlyLion at 2022/1/19 11:22
 */
interface AbstractMutableColumnVector<A> : AbstractMutableMatrix<A>, MutableVectorLike<A>, AbstractColumnVector<A> {

    override fun setElementAtUnsafe(row: UInt, column: UInt, a: A) {
        setVectorElementAtUnsafe(row, a)
    }

}