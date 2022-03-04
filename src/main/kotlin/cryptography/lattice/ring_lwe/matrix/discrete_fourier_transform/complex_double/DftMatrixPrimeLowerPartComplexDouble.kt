package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPI
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPrimeLowerPart
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftPaddingZeroColumnVector
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftRepeatRowMatrix
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.complex_number.ComplexNumber
import math.complex_number.FieldComplexNumberDouble
import math.integer.uint.nextTwoPositivePower
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.concrete.DiagonalMatrix
import math.martix.mutable.AbstractMutableMatrix
import math.martix.zeroMutableMatrix
import util.stdlib.lazyAssert2
import util.stdlib.list

/**
 * Created by CowardlyLion at 2022/2/3 22:20
 */
class DftMatrixPrimeLowerPartComplexDouble(root: RootUIntPI<ComplexNumber<Double>>) : DftMatrixPrimeLowerPart<ComplexNumber<Double>>(root) {

    init {
        lazyAssert2 {
            assert(2uL * rows.toULong() - 1u <= Int.MAX_VALUE.toUInt())
            assert(root.ring == FieldComplexNumberDouble)
        }
    }

    val g = root.order.primeField.firstGenerator

    val dft: DftMatrixPPI<ComplexNumber<Double>>
    val dftInv: AbstractSquareMatrix<ComplexNumber<Double>>
    val diag_dft_rgInvPadding: AbstractMatrix<ComplexNumber<Double>>

    val twoPower = nextTwoPositivePower(2u * rows - 1u)     //speedup when size is already 2^k, or leave it alone to prevent so-called 'timing-attack'? //situations that p = 2^k+1 is very few.

    init {
        dft = FieldComplexNumberDouble.dft(twoPower)
        dftInv = dft.inverse
        val rgInv: List<ComplexNumber<Double>> = list(root.order.value - 1u) { i -> root.cachedPower(g.cachedInversePower(i).residue) }
        val rgInvPadding = DftPaddingZeroColumnVector(ring, rgInv, twoPower.value)
        diag_dft_rgInvPadding = DiagonalMatrix(ring, (dft * rgInvPadding).columnListAt(0u))
    }

    override fun timesImpl(matrix: AbstractMatrix<ComplexNumber<Double>>): AbstractMatrix<ComplexNumber<Double>> {
        val mgList = list(rows) { i -> matrix.rowListAt(g.cachedPower(i).residue) }
        val mgListRepeat = DftRepeatRowMatrix(ring, mgList, twoPower.value, matrix.columns)
        val convolution = dftInv.times(diag_dft_rgInvPadding.times(dft.times(mgListRepeat)))
        val result = ring.zeroMutableMatrix(rows, matrix.columns)
        for (i in 0u until rows) {
            val row = g.cachedInversePower(i).residue - 1u
            for (j in 0u until matrix.columns) {
                result.setElementAtUnsafe(row, j, ring.add(matrix.elementAtUnsafe(0u, j), convolution.elementAtUnsafe(i, j)))
            }
        }
        return result
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<ComplexNumber<Double>>): AbstractMatrix<ComplexNumber<Double>> = coroutineScope {
        val mgList = list(rows) { i -> matrix.rowListAt(g.cachedPower(i).residue) }
        val mgListRepeat = DftRepeatRowMatrix(ring, mgList, twoPower.value, matrix.columns)
        val convolution = dftInv.timesRowParallel(diag_dft_rgInvPadding.timesRowParallel(dft.timesRowParallel(mgListRepeat)))
        val result = ring.zeroMutableMatrix(rows, matrix.columns)
        for (i in 0u until rows) {
            launch {
                val row = g.cachedInversePower(i).residue - 1u
                for (j in 0u until matrix.columns) {
                    result.setElementAtUnsafe(row, j, ring.add(matrix.elementAtUnsafe(0u, j), convolution.elementAtUnsafe(i, j)))
                }
            }
        }
        result
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<ComplexNumber<Double>>, dest: AbstractMutableMatrix<ComplexNumber<Double>>) {
        val mgList = list(rows) { i -> matrix.rowListAt(g.cachedPower(i).residue) }
        val mgListRepeat = DftRepeatRowMatrix(ring, mgList, twoPower.value, matrix.columns)
        val convolution = dftInv.times(diag_dft_rgInvPadding.times(dft.times(mgListRepeat)))
        for (i in 0u until rows) {
            val row = g.cachedInversePower(i).residue - 1u
            for (j in 0u until matrix.columns) {
                dest.setElementAtUnsafe(row, j, ring.add(matrix.elementAtUnsafe(0u, j), convolution.elementAtUnsafe(i, j)))
            }
        }
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<ComplexNumber<Double>>, dest: AbstractMutableMatrix<ComplexNumber<Double>>) = coroutineScope {
        val mgList = list(rows) { i -> matrix.rowListAt(g.cachedPower(i).residue) }
        val mgListRepeat = DftRepeatRowMatrix(ring, mgList, twoPower.value, matrix.columns)
        val convolution = dftInv.timesRowParallel(diag_dft_rgInvPadding.timesRowParallel(dft.timesRowParallel(mgListRepeat)))
        for (i in 0u until rows) {
            launch {
                val row = g.cachedInversePower(i).residue - 1u
                for (j in 0u until matrix.columns) {
                    dest.setElementAtUnsafe(row, j, ring.add(matrix.elementAtUnsafe(0u, j), convolution.elementAtUnsafe(i, j)))
                }
            }
        }
    }
}