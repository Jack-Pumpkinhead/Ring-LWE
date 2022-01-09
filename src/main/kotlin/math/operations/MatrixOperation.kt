package math.operations

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.CRing
import math.martix.AbstractMatrix
import math.martix.matrix
import math.martix.matrixRowParallel
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/9 18:15
 */

fun <A> multiply(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>): AbstractMatrix<A> {
    require(mA.ring == mB.ring)
    require(mA.columns == mB.rows)
    return mA.ring.multiplyUnsafe(mA, mB)
}

/**
 * Used as base for testing correctness of other (optimized) matrix multiplication method.
 * */
fun <A> CRing<A>.multiplyUnsafe(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>): AbstractMatrix<A> {
    return matrix(mA.rows, mB.columns) { i, k ->
        var sum = zero
        for (j in 0u until mA.columns) {
            sum = add(sum, multiply(mA.elementAtUnsafe(i, j), mB.elementAtUnsafe(j, k)))
        }
        sum
    }.downCast()
}

fun <A> multiplyTo(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>): AbstractMatrix<A> {
    require(mA.ring == mB.ring)
    require(mA.ring == dest.ring)
    require(mA.columns == mB.rows)
    require(mA.rows == dest.rows)
    require(mB.columns == dest.columns)
    return mA.ring.multiplyToUnsafe(mA, mB, dest)
}

fun <A> CRing<A>.multiplyToUnsafe(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>): AbstractMatrix<A> {
    for (i in 0u until dest.rows) {
        for (k in 0u until dest.columns) {
            var sum = zero
            for (j in 0u until mA.columns) {
                sum = add(sum, multiply(mA.elementAtUnsafe(i, j), mB.elementAtUnsafe(j, k)))
            }
            dest.setElementAtSafe(i, k, sum)
        }
    }
    return dest.downCast()
}

suspend fun <A> multiplyRowParallel(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>): AbstractMatrix<A> {
    require(mA.ring == mB.ring)
    require(mA.columns == mB.rows)
    return mA.ring.multiplyRowParallelUnsafe(mA, mB).downCast()
}

suspend fun <A> CRing<A>.multiplyRowParallelUnsafe(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>): AbstractMatrix<A> {
    return matrixRowParallel(mA.rows, mB.columns) { i, k ->
        var sum = zero
        for (j in 0u until mA.columns) {
            sum = add(sum, multiply(mA.elementAtUnsafe(i, j), mB.elementAtUnsafe(j, k)))    //maybe faster by caching matrix[i][j], but improvement on speed may insignificant.
        }
        sum
    }.downCast()
}


suspend fun <A> multiplyToParallel(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>): AbstractMatrix<A> {
    require(mA.ring == mB.ring)
    require(mA.columns == mB.rows)
    return mA.ring.multiplyToParallelUnsafe(mA, mB, dest)
}

/**
 * By structural concurrency of coroutineScope, no need to joinAll() launched task.
 * Concurrently setting the value in an array would not throw ConcurrentModificationException
 * */
suspend fun <A> CRing<A>.multiplyToParallelUnsafe(mA: AbstractMatrix<A>, mB: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>): AbstractMatrix<A> = coroutineScope {
    for (i in 0u until dest.rows) {
        launch {
            for (k in 0u until dest.columns) {
                var sum = zero
                for (j in 0u until mA.columns) {
                    sum = add(sum, multiply(mA.elementAtUnsafe(i, j), mB.elementAtUnsafe(j, k)))
                }
                dest.setElementAtSafe(i, k, sum)
            }
        }
    }
    dest
}.downCast()