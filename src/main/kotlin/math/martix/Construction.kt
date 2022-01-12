package math.martix

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import math.abstract_structure.CRing
import math.martix.concrete.Constant
import math.martix.concrete.OrdinaryMatrix
import math.martix.mutable.ArrayMatrix
import math.martix.mutable.MutableMatrix
import math.martix.mutable.MutableSizeMatrix
import math.martix.tensor.FormalKroneckerProduct

/**
 * Created by CowardlyLion at 2022/1/8 13:31
 */

typealias ConstructorOfList<A, L> = ((Int), (Int) -> A) -> L

//size, constructor
fun <A, L1 : List<A>, L0 : List<L1>> nested(
    s0: UInt,
    c0: ConstructorOfList<L1, L0>,
    s1: UInt,
    c1: ConstructorOfList<A, L1>,
    generator: (UInt, UInt) -> A
): L0 {
    return c0(s0.toInt()) { i ->
        c1(s1.toInt()) { j ->
            generator(i.toUInt(), j.toUInt())
        }
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


fun <A> nestedLL(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): List<List<A>> = nested(rows, ::List, columns, ::List, generator)
fun <A> nestedLML(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): List<MutableList<A>> = nested(rows, ::List, columns, ::MutableList, generator)
fun <A> nestedMLML(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): MutableList<MutableList<A>> = nested(rows, ::MutableList, columns, ::MutableList, generator)

//List of Array
inline fun <reified A> nestedLA(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): List<Array<A>> {
    return List(rows.toInt()) { row ->
        Array(columns.toInt()) { column ->
            generator(row.toUInt(), column.toUInt())
        }
    }
}


/**
 * List of List with parallel row construction
 * https://kotlinlang.org/docs/composing-suspending-functions.html#structured-concurrency-with-async
 * (cite) ..., if something goes wrong inside the code of the concurrentSum function, and it throws an exception, all the coroutines that were launched in its scope will be cancelled.
 * */
suspend fun <A> nestedLLRP(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): List<List<A>> = listParallel(rows) { row -> List(columns.toInt()) { column -> generator(row, column.toUInt()) } }
suspend fun <A> nestedLMLRP(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A): List<MutableList<A>> = listParallel(rows) { row -> MutableList(columns.toInt()) { column -> generator(row, column.toUInt()) } }
suspend inline fun <reified A> nestedLARP(rows: UInt, columns: UInt, crossinline generator: (UInt, UInt) -> A): List<Array<A>> = listParallel(rows) { row -> Array(columns.toInt()) { column -> generator(row, column.toUInt()) } }


fun <A> CRing<A>.matrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = OrdinaryMatrix(this, nestedLL(rows, columns, generator))
fun <A> CRing<A>.mutableMatrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = MutableMatrix(this, nestedLML(rows, columns, generator))
fun <A> CRing<A>.mutableSizeMatrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = MutableSizeMatrix(this, rows, columns, nestedMLML(rows, columns, generator))
inline fun <reified A> CRing<A>.mutableArrayMatrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = ArrayMatrix(this, nestedLA(rows, columns, generator))

suspend fun <A> CRing<A>.matrixRowParallel(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = OrdinaryMatrix(this, nestedLLRP(rows, columns, generator))
suspend fun <A> CRing<A>.mutableMatrixRowParallel(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = MutableMatrix(this, nestedLMLRP(rows, columns, generator))
suspend inline fun <reified A> CRing<A>.mutableArrayMatrixRowParallel(rows: UInt, columns: UInt, crossinline generator: (UInt, UInt) -> A) = ArrayMatrix(this, nestedLARP(rows, columns, generator))

fun <A> CRing<A>.constantMatrix(a: A) = Constant(this, a)
fun <A> CRing<A>.identityMatrix(size: UInt) = IdentityMatrix(this, size)
fun <A> CRing<A>.identityOrdinaryMatrix(size: UInt) = matrix(size, size) { i, j -> if (i == j) one else zero }
fun <A> CRing<A>.zeroMatrix(rows: UInt, columns: UInt) = ZeroMatrix(this, rows, columns)
fun <A> CRing<A>.zeroMutableMatrix(rows: UInt, columns: UInt) = mutableMatrix(rows, columns) { _, _ -> zero }
fun <A> CRing<A>.zeroMutableSizeMatrix(rows: UInt, columns: UInt) = mutableSizeMatrix(rows, columns) { _, _ -> zero }
fun <A> CRing<A>.scalaMatrix(size: UInt, a: A) = matrix(size, size) { i, j -> if (i == j) a else zero }
fun <A> CRing<A>.diagonalMatrix(list: List<A>) = matrix(list.size.toUInt(), list.size.toUInt()) { i, j -> if (i == j) list[i.toInt()] else zero }
fun <A> CRing<A>.formalKroneckerProduct(vararg matrices: AbstractMatrix<A>): FormalKroneckerProduct<A> = FormalKroneckerProduct(this, matrices.toList())
fun <A> CRing<A>.formalKroneckerProduct(matrices: List<AbstractMatrix<A>>): FormalKroneckerProduct<A> = FormalKroneckerProduct(this, matrices)


inline fun <reified A> AbstractMatrix<A>.toMutableArrayMatrix(): ArrayMatrix<A> {
    return ring.mutableArrayMatrix(rows, columns) { i, j -> elementAt(i, j) }
}

inline fun <reified A> OrdinaryMatrix<A>.toMutableArrayMatrix(): ArrayMatrix<A> {
    return ArrayMatrix(ring, matrix.map { row -> row.toTypedArray() })
}
