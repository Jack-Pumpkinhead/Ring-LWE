package math.timing

import math.martix.AbstractMatrix
import math.operation.matrixEquals

/**
 * Created by CowardlyLion at 2022/1/29 11:33
 */
class ManyMatricesMultiplicationTiming<A>(override val tasks: List<Task<ManyMatrices<A>, AbstractMatrix<A>>>) : EqualTaskTiming<ManyMatrices<A>, AbstractMatrix<A>> {

    constructor(vararg tasks: Task<ManyMatrices<A>, AbstractMatrix<A>>) : this(tasks.toList())

    override fun equals(a: AbstractMatrix<A>, b: AbstractMatrix<A>): Boolean = matrixEquals(a, b)

}