package math.martix

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import math.abstract_structure.CRing
import math.martix.mutable.ArrayMatrix
import math.martix.mutable.MutableMatrix
import math.martix.tensor.FormalSquareTensorProduct

/**
 * Created by CowardlyLion at 2022/1/8 13:31
 */


fun <A> nestedListList(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): List<List<A>> {
    return List(rows.toInt()) { row ->
        List(columns.toInt()) { column ->
            generator(row.toUInt(), column.toUInt())
        }
    }
}

fun <A> nestedListMutableList(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): List<MutableList<A>> {
    return List(rows.toInt()) { row ->
        MutableList(columns.toInt()) { column ->
            generator(row.toUInt(), column.toUInt())
        }
    }
}

inline fun <reified A> nestedListArray(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): List<Array<A>> {
    return List(rows.toInt()) { row ->
        Array(columns.toInt()) { column ->
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

suspend fun <A> nestedListMutableListRowParallel(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): List<MutableList<A>> = listParallel(rows) { row ->
    MutableList(columns.toInt()) { column ->
        generator(row, column.toUInt())
    }
}

suspend inline fun <reified A> nestedListArrayRowParallel(rows: UInt, columns: UInt, crossinline generator: (UInt, UInt) -> A): List<Array<A>> = listParallel(rows) { row ->
    Array(columns.toInt()) { column ->
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

fun <A> CRing<A>.matrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = OrdinaryMatrix(this, nestedListList(rows, columns, generator))
fun <A> CRing<A>.mutableMatrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = MutableMatrix(this, nestedListMutableList(rows, columns, generator))
inline fun <reified A> CRing<A>.mutableArrayMatrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = ArrayMatrix(this, nestedListArray(rows, columns, generator))

suspend fun <A> CRing<A>.matrixRowParallel(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = OrdinaryMatrix(this, nestedListRowParallel(rows, columns, generator))
suspend fun <A> CRing<A>.mutableMatrixRowParallel(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = MutableMatrix(this, nestedListMutableListRowParallel(rows, columns, generator))
suspend inline fun <reified A> CRing<A>.mutableArrayMatrixRowParallel(rows: UInt, columns: UInt, crossinline generator: (UInt, UInt) -> A) = ArrayMatrix(this, nestedListArrayRowParallel(rows, columns, generator))

fun <A> CRing<A>.identityMatrix(size: UInt) = matrix(size, size) { i, j -> if (i == j) one else zero }
fun <A> CRing<A>.scalaMatrix(size: UInt, a: A) = matrix(size, size) { i, j -> if (i == j) a else zero }
fun <A> CRing<A>.diagonalMatrix(list: List<A>) = matrix(list.size.toUInt(), list.size.toUInt()) { i, j -> if (i == j) list[i.toInt()] else zero }
fun <A> CRing<A>.formalSquareTensorProduct(vararg matrices: AbstractMatrix<A>): FormalSquareTensorProduct<A> = FormalSquareTensorProduct(this, matrices.toList())


inline fun <reified A> AbstractMatrix<A>.toMutableArrayMatrix(): ArrayMatrix<A> {
    return ring.mutableArrayMatrix(rows, columns) { i, j -> elementAt(i, j) }
}

inline fun <reified A> OrdinaryMatrix<A>.toMutableArrayMatrix(): ArrayMatrix<A> {
    return ArrayMatrix(ring, matrix.map { row -> row.toTypedArray() })
}
