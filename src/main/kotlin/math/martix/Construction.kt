package math.martix

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import math.abstract_structure.Ring
import math.coding.permutation.Permutation
import math.martix.concrete.*
import math.martix.mutable.ArrayMatrix
import math.martix.mutable.MutableMatrix
import math.martix.mutable.MutableSizeMatrix
import math.martix.tensor.FormalKroneckerProduct
import math.martix.tensor.WhiskeredKroneckerProduct

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


fun <A> Ring<A>.rowVector(columns: UInt, generator: (UInt) -> A) = RowVector(this, List(columns.toInt()) { i -> generator(i.toUInt()) })
suspend fun <A> Ring<A>.rowVectorParallel(columns: UInt, generator: (UInt) -> A) = RowVector(this, listParallel(columns, generator))
fun <A> Ring<A>.columnVector(rows: UInt, generator: (UInt) -> A) = ColumnVector(this, List(rows.toInt()) { i -> generator(i.toUInt()) })
suspend fun <A> Ring<A>.columnVectorParallel(rows: UInt, generator: (UInt) -> A) = ColumnVector(this, listParallel(rows, generator))

fun <A> Ring<A>.matrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = OrdinaryMatrix(this, nestedLL(rows, columns, generator))
fun <A> Ring<A>.mutableMatrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = MutableMatrix(this, nestedLML(rows, columns, generator))
fun <A> Ring<A>.mutableSizeMatrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = MutableSizeMatrix(this, rows, columns, nestedMLML(rows, columns, generator))
inline fun <reified A> Ring<A>.mutableArrayMatrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = ArrayMatrix(this, nestedLA(rows, columns, generator))

suspend fun <A> Ring<A>.matrixRowParallel(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = OrdinaryMatrix(this, nestedLLRP(rows, columns, generator))
suspend fun <A> Ring<A>.mutableMatrixRowParallel(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = MutableMatrix(this, nestedLMLRP(rows, columns, generator))
suspend inline fun <reified A> Ring<A>.mutableArrayMatrixRowParallel(rows: UInt, columns: UInt, crossinline generator: (UInt, UInt) -> A) = ArrayMatrix(this, nestedLARP(rows, columns, generator))

fun <A> Ring<A>.constantMatrix(a: A) = Constant(this, a)
fun <A> Ring<A>.identityMatrix(size: UInt) = IdentityMatrix(this, size)
fun <A> Ring<A>.identityOrdinaryMatrix(size: UInt) = matrix(size, size) { i, j -> if (i == j) one else zero }
fun <A> Ring<A>.zeroMatrix(rows: UInt, columns: UInt) = ZeroMatrix(this, rows, columns)
fun <A> Ring<A>.zeroMutableMatrix(rows: UInt, columns: UInt) = mutableMatrix(rows, columns) { _, _ -> zero }
fun <A> Ring<A>.zeroMutableSizeMatrix(rows: UInt, columns: UInt) = mutableSizeMatrix(rows, columns) { _, _ -> zero }
fun <A> Ring<A>.scalaMatrix(size: UInt, a: A) = DiagonalMatrix(this, List(size.toInt()) { a })
fun <A> Ring<A>.diagonalMatrix(list: List<A>) = DiagonalMatrix(this, list)
fun <A> Ring<A>.diagonalMatrix(size: UInt, generator: (UInt) -> A) = DiagonalMatrix(this, List(size.toInt()) { i -> generator(i.toUInt()) })
fun <A> Ring<A>.formalKroneckerProduct(vararg matrices: AbstractMatrix<A>): FormalKroneckerProduct<A> = FormalKroneckerProduct(this, matrices.toList())
fun <A> Ring<A>.formalKroneckerProduct(matrices: List<AbstractMatrix<A>>): FormalKroneckerProduct<A> = FormalKroneckerProduct(this, matrices)
fun <A> Ring<A>.permutationMatrix(permutation: Permutation) = PermutationMatrix(this, permutation)
fun <A> Ring<A>.whiskered(l: BigInteger, matrix: AbstractMatrix<A>, r: BigInteger) = WhiskeredKroneckerProduct(this, l, matrix, r)

inline fun <reified A> AbstractMatrix<A>.toMutableArrayMatrix(): ArrayMatrix<A> {
    return ring.mutableArrayMatrix(rows, columns) { i, j -> elementAt(i, j) }
}

inline fun <reified A> OrdinaryMatrix<A>.toMutableArrayMatrix(): ArrayMatrix<A> {
    return ArrayMatrix(ring, matrix.map { row -> row.toTypedArray() })
}

//    there are (matrices.size)! ways (permutations) of decomposition, use one that compute m0 first.
fun <A> Ring<A>.decomposeFormalKroneckerProduct(matrices: List<AbstractMatrix<A>>): List<AbstractMatrix<A>> = when (matrices.size) {
    0    -> listOf(identityMatrix(1u))
    1    -> matrices
    else -> {
        var l = BigInteger.ONE
        for (m in matrices) {
            l *= m.rows.toBigInteger()
        }
        var r = BigInteger.ONE

        val result = mutableListOf<AbstractMatrix<A>>()
        for (i in matrices.size - 1 downTo 0) {
            l /= matrices[i].rows.toBigInteger()
            result += WhiskeredKroneckerProduct(this, l, matrices[i], r)
            r *= matrices[i].columns.toBigInteger()
        }
        result
    }
}
