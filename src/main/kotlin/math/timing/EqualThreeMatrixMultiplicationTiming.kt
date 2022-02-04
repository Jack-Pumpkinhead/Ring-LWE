package math.timing

import math.martix.AbstractMatrix
import math.operation.matrixEquals

/**
 * Created by CowardlyLion at 2022/1/28 23:30
 */
class EqualThreeMatrixMultiplicationTiming<A>(override val tasks: List<Task<ThreeMatrix<A>, AbstractMatrix<A>>>) : EqualTaskTiming<ThreeMatrix<A>, AbstractMatrix<A>> {

    constructor(vararg tasks: Task<ThreeMatrix<A>, AbstractMatrix<A>>) : this(tasks.toList())

    override fun equals(a: AbstractMatrix<A>, b: AbstractMatrix<A>): Boolean = matrixEquals(a, b)

}