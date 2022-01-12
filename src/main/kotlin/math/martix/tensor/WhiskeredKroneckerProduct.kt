package math.martix.tensor

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.CRing
import math.martix.AbstractMatrix
import math.martix.identityMatrix
import math.martix.mutable.AbstractMutableMatrix
import math.martix.zeroMutableMatrix

/**
 * Created by CowardlyLion at 2022/1/9 17:39
 *
 * represent a matrix in the form I_l ⊗ M ⊗ I_r
 */
class WhiskeredKroneckerProduct<A>(ring: CRing<A>, val l: UInt, val mA: AbstractMatrix<A>, val r: UInt) : FormalKroneckerProduct<A>(ring, listOf(ring.identityMatrix(l), mA, ring.identityMatrix(r))) {

    override fun timesImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        val dest = ring.zeroMutableMatrix(this.rows, matrix.columns)
        multiplyToImpl(matrix, dest)
        return dest.downCast()
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<A>): AbstractMatrix<A> {
        val dest = ring.zeroMutableMatrix(this.rows, matrix.columns)
        multiplyToParallelImpl(matrix, dest)
        return dest.downCast()
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) {
        for (il in 0u until l) {
            for (ir in 0u until r) {
                val submatrix = matrix.sparseSubmatrixView(il * mA.columns * r + ir, r, mA.columns)
                val destSubmatrix = dest.mutableSparseSubmatrixView(il * mA.rows * r + ir, r, mA.rows)
                mA.multiplyTo(submatrix, destSubmatrix)
            }
        }
    }

    override suspend fun multiplyToParallelImpl(matrix: AbstractMatrix<A>, dest: AbstractMutableMatrix<A>) = coroutineScope {
        for (il in 0u until l) {
            for (ir in 0u until r) {
                launch {
                    val submatrix = matrix.sparseSubmatrixView(il * mA.columns * r + ir, r, mA.columns)
                    val destSubmatrix = dest.mutableSparseSubmatrixView(il * mA.rows * r + ir, r, mA.rows)
                    mA.multiplyTo(submatrix, destSubmatrix)
                }
            }
        }
    }
}