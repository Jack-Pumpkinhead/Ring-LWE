package math.martix

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import math.abstract_structure.Ring

/**
 * Created by CowardlyLion at 2022/1/8 13:31
 */


fun <A> nestedList(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): List<List<A>> {
    return List(rows.toInt()) { row ->
        List(columns.toInt()) { column ->
            generator(row.toUInt(), column.toUInt())
        }
    }
}

/**
 * https://kotlinlang.org/docs/composing-suspending-functions.html#structured-concurrency-with-async
 * (cite) ..., if something goes wrong inside the code of the concurrentSum function, and it throws an exception, all the coroutines that were launched in its scope will be cancelled.
 * */
suspend fun <A> nestedListRowParallel(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): List<List<A>> = listParallel(rows) { row ->
    List(columns.toInt()) { column ->
        generator(row, column.toUInt())
    }
}

suspend fun <A> listParallel(size: UInt, generator: (UInt) -> A): List<A> = coroutineScope {
    val list = List(size.toInt()) { i ->
        async {
            generator(i.toUInt())
        }
    }
    list.awaitAll()
}


fun <A> Ring<A>.matrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): Matrix<A> = OrdinaryMatrix(this, nestedList(rows, columns, generator))
suspend fun <A> Ring<A>.matrixRowParallel(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): Matrix<A> = OrdinaryMatrix(this, nestedListRowParallel(rows, columns, generator))
fun <A> Ring<A>.identityMatrix(size: UInt): Matrix<A> = matrix(size, size) { i, j -> if (i == j) one else zero }

