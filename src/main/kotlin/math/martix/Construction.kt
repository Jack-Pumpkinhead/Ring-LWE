package math.martix

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import math.abstract_structure.Ring
import math.coding.permutation.Permutation
import math.martix.concrete.*
import math.martix.mutable.MutableColumnVector
import math.martix.mutable.MutableMatrix
import math.martix.mutable.MutableRowVector
import math.martix.mutable.MutableSizeMatrix
import math.martix.tensor.*
import util.stdlib.mutableList

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

fun <A> Ring<A>.matrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = OrdinaryMatrix(this, rows, columns, nestedLL(rows, columns, generator))
fun <A> Ring<A>.squareMatrix(size: UInt, generator: (UInt, UInt) -> A) = OrdinarySquareMatrix(this, size, nestedLL(size, size, generator))
fun <A> Ring<A>.mutableMatrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = MutableMatrix(this, rows, columns, nestedLML(rows, columns, generator))
fun <A> Ring<A>.mutableColumnVector(size: UInt, generator: (UInt) -> A) = MutableColumnVector(this, mutableList(size, generator))
fun <A> Ring<A>.mutableRowVector(size: UInt, generator: (UInt) -> A) = MutableRowVector(this, mutableList(size, generator))
fun <A> Ring<A>.mutableSizeMatrix(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = MutableSizeMatrix(this, rows, columns, nestedMLML(rows, columns, generator))

suspend fun <A> Ring<A>.matrixRowParallel(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = OrdinaryMatrix(this, rows, columns, nestedLLRP(rows, columns, generator))
suspend fun <A> Ring<A>.mutableMatrixRowParallel(rows: UInt, columns: UInt, generator: (UInt, UInt) -> A) = MutableMatrix(this, rows, columns, nestedLMLRP(rows, columns, generator))

fun <A> Ring<A>.constantMatrix(a: A) = Constant(this, a)
fun <A> Ring<A>.scalarMatrix(size: UInt, a: A) = ScalarMatrix(this, size, a)
fun <A> Ring<A>.identityMatrix(size: UInt) = IdentityMatrix(this, size)
fun <A> Ring<A>.identityOrdinaryMatrix(size: UInt) = matrix(size, size) { i, j -> if (i == j) one else zero }
fun <A> Ring<A>.zeroMatrix(rows: UInt, columns: UInt) = ZeroMatrix(this, rows, columns)
fun <A> Ring<A>.zeroMutableMatrix(rows: UInt, columns: UInt) = mutableMatrix(rows, columns) { _, _ -> zero }
fun <A> Ring<A>.zeroMutableColumnVector(size: UInt) = mutableColumnVector(size) { _ -> zero }
fun <A> Ring<A>.zeroMutableRowVector(size: UInt) = mutableRowVector(size) { _ -> zero }
fun <A> Ring<A>.zeroMutableSizeMatrix(rows: UInt, columns: UInt) = mutableSizeMatrix(rows, columns) { _, _ -> zero }
fun <A> Ring<A>.scalarDiagonalMatrix(size: UInt, a: A) = DiagonalMatrix(this, List(size.toInt()) { a })
fun <A> Ring<A>.diagonalMatrix(list: List<A>) = DiagonalMatrix(this, list)
fun <A> Ring<A>.diagonalMatrix(size: UInt, generator: (UInt) -> A) = DiagonalMatrix(this, List(size.toInt()) { i -> generator(i.toUInt()) })
fun <A> Ring<A>.formalProduct(vararg matrices: AbstractMatrix<A>): FormalProduct<A> = FormalProduct(this, matrices.first().rows, matrices.last().columns, matrices.toList())
fun <A> Ring<A>.formalProduct(vararg matrices: AbstractSquareMatrix<A>): SquareFormalProduct<A> = SquareFormalProduct(this, matrices.first().size, matrices.toList())
fun <A> Ring<A>.formalKroneckerProduct(size: UInt, vararg matrices: AbstractSquareMatrix<A>): SquareFormalKroneckerProduct<A> = SquareFormalKroneckerProduct(this, size, matrices.toList())
fun <A> Ring<A>.formalKroneckerProduct(rows: UInt, columns: UInt, vararg matrices: AbstractMatrix<A>): FormalKroneckerProduct<A> = FormalKroneckerProduct(this, rows, columns, matrices.toList())
fun <A> Ring<A>.formalKroneckerProduct(size: UInt, matrices: List<AbstractSquareMatrix<A>>): SquareFormalKroneckerProduct<A> = SquareFormalKroneckerProduct(this, size, matrices)
fun <A> Ring<A>.formalKroneckerProduct(rows: UInt, columns: UInt, matrices: List<AbstractMatrix<A>>): FormalKroneckerProduct<A> = FormalKroneckerProduct(this, rows, columns, matrices)
fun <A> Ring<A>.permutationMatrix(permutation: Permutation) = PermutationMatrix(this, permutation)
fun <A> Ring<A>.whiskered(rows: UInt, columns: UInt, l: UInt, matrix: AbstractMatrix<A>, r: UInt) = WhiskeredKroneckerProduct(this, rows, columns, l, matrix, r)
fun <A> Ring<A>.whiskered(size: UInt, l: UInt, matrix: AbstractSquareMatrix<A>, r: UInt) = SquareWhiskeredKroneckerProduct(this, size, l, matrix, r)

