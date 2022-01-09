package math.martix

import math.abstract_structure.CRing

/**
 * Created by CowardlyLion at 2022/1/8 17:03
 */
class RowVectorView<A>(ring: CRing<A>, val matrix: AbstractMatrix<A>, val row: UInt) : AbstractRowVector<A>(ring, matrix.columns) {

    override fun elementAtUnsafe(row: UInt, column: UInt): A = matrix.elementAt(this.row, column)

}