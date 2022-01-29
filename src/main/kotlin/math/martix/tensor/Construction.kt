package math.martix.tensor

import math.martix.AbstractMatrix
import math.martix.mutable.AbstractMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/9 13:44
 */

fun <A> AbstractMatrix<A>.rowSparseSubmatrixView(rowBase: UInt, rowSpacing: UInt, rows: UInt): RowSparseSubmatrixView<A> = RowSparseSubmatrixView(ring, this, rowBase, rowSpacing, rows)
fun <A> AbstractMutableMatrix<A>.mutableRowSparseSubmatrixView(rowBase: UInt, rowSpacing: UInt, rows: UInt): MutableRowSparseSubmatrixView<A> = MutableRowSparseSubmatrixView(ring, this, rowBase, rowSpacing, rows)