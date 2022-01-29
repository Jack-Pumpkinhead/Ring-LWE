package math.martix.mutable

import math.martix.AbstractRowVector
import math.vector.MutableVectorLike

/**
 * Created by CowardlyLion at 2022/1/28 17:13
 */
interface AbstractMutableRowVector<A> : AbstractMutableMatrix<A>, MutableVectorLike<A>, AbstractRowVector<A> {

    override fun setElementAtUnsafe(row: UInt, column: UInt, a: A) {
        setVectorElementAtUnsafe(column, a)
    }

}