package math.martix

import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/8 17:03
 */
abstract class AbstractRowVector<A>(ring: Ring<A>, columns: UInt) : AbstractMatrix<A>(ring, 1u, columns), VectorLike<A> {

    override fun vectorElementAt(index: UInt): A {
        return elementAt(0u, index)
    }

    override fun vectorElementAtUnsafe(index: UInt): A {
        return elementAtUnsafe(0u, index)
    }


    fun innerProduct(vector: AbstractColumnVector<A>): A {
        require(this.columns == vector.rows)
        var sum = ring.zero
        for (i in 0u until columns) {
            sum = ring.add(sum, ring.multiply(this.elementAtUnsafe(0u, i), vector.elementAtUnsafe(i, 0u)))
        }
        return sum
    }

}