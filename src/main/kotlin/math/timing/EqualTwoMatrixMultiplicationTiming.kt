package math.timing

import math.martix.AbstractMatrix
import math.operation.matrixEquals

/**
 * Created by CowardlyLion at 2022/1/28 23:50
 */
class EqualTwoMatrixMultiplicationTiming<A> : EqualTaskTiming<TwoMatrix<A>, AbstractMatrix<A>> {

    override fun equals(a: AbstractMatrix<A>, b: AbstractMatrix<A>): Boolean = matrixEquals(a,b)

    override val tasks: List<Task<TwoMatrix<A>, AbstractMatrix<A>>> = twoMatrixMultiplicationTasks()

}