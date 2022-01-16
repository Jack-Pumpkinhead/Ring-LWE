package math.martix

import math.abstract_structure.CRing

/**
 * Created by CowardlyLion at 2022/1/8 17:03
 */
abstract class AbstractRowVector<A>(ring: CRing<A>, columns: UInt) : AbstractMatrix<A>(ring, 1u, columns) {

    fun innerProduct(vector: AbstractColumnVector<A>): A {
        require(this.columns == vector.rows)
        var sum = ring.zero
        for (i in 0u until columns) {
            sum = ring.add(sum, ring.multiply(this.elementAtUnsafe(0u, i), vector.elementAtUnsafe(i, 0u)))
        }
        return sum
    }

}