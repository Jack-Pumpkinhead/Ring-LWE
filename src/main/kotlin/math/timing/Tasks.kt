package math.timing

import math.martix.AbstractMatrix

/**
 * Created by CowardlyLion at 2022/2/4 17:02
 */

fun <A> twoMatrixMultiplicationTasks(): List<Task<TwoMatrix<A>, AbstractMatrix<A>>> = listOf(
    Task(" *  ") { (m, x) -> m * x },
    Task(" *p ") { (m, x) -> m.timesRowParallel(x) },
    Task(" * t") { (m, x) -> m.multiplyToNewMutableMatrix(x) },
    Task(" *pt") { (m, x) -> m.multiplyToNewMutableMatrixRowParallel(x) },
    Task("d*  ") { (m, x) -> math.operation.multiply(m, x) },   //留一个检验乘法正确
//        Task("d*p ") { (m, x) -> math.operation.multiplyRowParallel(m, x) },
//        Task("d* t") { (m, x) -> math.operation.multiplyToNewMutableMatrix(m, x) },
//        Task("d*pt") { (m, x) -> math.operation.multiplyToNewMutableMatrixRowParallel(m, x) }
)