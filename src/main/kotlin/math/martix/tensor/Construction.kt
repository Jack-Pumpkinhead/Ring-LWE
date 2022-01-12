package math.martix.tensor

import math.martix.AbstractMatrix
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/9 13:44
 */

fun <A> AbstractMatrix<A>.sparseSubmatrixView(rowBase: UInt, rowSpacing: UInt, rows: UInt): SparseSubmatrixView<A> = SparseSubmatrixView(ring, this, rowBase, rowSpacing, rows)
fun <A> AbstractMutableMatrix<A>.mutableSparseSubmatrixView(rowBase: UInt, rowSpacing: UInt, rows: UInt): MutableSparseSubmatrixView<A> = MutableSparseSubmatrixView(ring, this, rowBase, rowSpacing, rows)