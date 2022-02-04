package math.timing

import math.martix.AbstractMatrix

/**
 * Created by CowardlyLion at 2022/2/4 17:06
 */
class TwoMatrixMultiplicationTiming<A> : TaskTiming<TwoMatrix<A>, AbstractMatrix<A>> {

    override val tasks: List<Task<TwoMatrix<A>, AbstractMatrix<A>>> = twoMatrixMultiplicationTasks()

}