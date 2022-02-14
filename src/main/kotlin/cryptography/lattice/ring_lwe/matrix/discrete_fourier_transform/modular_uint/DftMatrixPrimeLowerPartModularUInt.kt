package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPI
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPrimeLowerPart
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftPaddingZeroColumnVector
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftRepeatRowMatrix
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import math.abstract_structure.instance.FieldComplexNumberDouble
import math.abstract_structure.instance.FieldDouble
import math.complex_number.ComplexNumber
import math.complex_number.maxRoundingError
import math.complex_number.realComplexNumber
import math.complex_number.roundToLong
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.uint.nextTwoPositivePower
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.concrete.ColumnVector
import math.martix.concrete.DiagonalMatrix
import math.martix.mutable.AbstractMutableMatrix
import math.martix.zeroMutableMatrix
import util.stdlib.lazyAssert2
import util.stdlib.list

/**
 * Created by CowardlyLion at 2022/2/5 12:25
 */
class DftMatrixPrimeLowerPartModularUInt(root: RootUIntPI<ModularUInt>) : DftMatrixPrimeLowerPart<ModularUInt>(root) {

    init {
        lazyAssert2 {
            assert(2uL * rows.toULong() - 1u <= Int.MAX_VALUE.toUInt())
            assert(root.ring is FieldModularUInt)
        }
    }

    val g = root.order.primeField.firstGenerator

    val dft: DftMatrixPPI<ComplexNumber<Double>>
    val dftInv: AbstractSquareMatrix<ComplexNumber<Double>>
    val diag_dft_rgInvPadding: AbstractMatrix<ComplexNumber<Double>>

    val twoPower = nextTwoPositivePower(2u * rows - 1u)     //speedup when size is already 2^k, or leave it alone to prevent so-called 'timing-attack'?     //situations that p = 2^k+1 is very few.

    init {
        dft = FieldComplexNumberDouble.dft(twoPower)
        dftInv = dft.inverse
        val rgInv: List<ComplexNumber<Double>> = list(root.order.value - 1u) { i -> FieldDouble.realComplexNumber(root.cachedPower(g.cachedInversePower(i).residue).residue.toDouble()) }
        val rgInvPadding = DftPaddingZeroColumnVector(FieldComplexNumberDouble, rgInv, twoPower.value)
        diag_dft_rgInvPadding = DiagonalMatrix(FieldComplexNumberDouble, ((dft * rgInvPadding).downCast() as ColumnVector).vector)  //TODO casting to column vector maybe slow
    }

    override fun timesImpl(matrix: AbstractMatrix<ModularUInt>): AbstractMatrix<ModularUInt> {
        val mgList = list(rows) { i -> matrix.rowListAt(g.cachedPower(i).residue).map { FieldDouble.realComplexNumber(it.residue.toDouble()) } }
        val mgListRepeat = DftRepeatRowMatrix(FieldComplexNumberDouble, mgList, twoPower.value, matrix.columns)
        val convolution = dftInv.times(diag_dft_rgInvPadding.times(dft.times(mgListRepeat)))
        val result = ring.zeroMutableMatrix(rows, matrix.columns)
        maxRoundingError = 0.0
        for (i in 0u until rows) {
            val row = g.cachedInversePower(i).residue - 1u
            for (j in 0u until matrix.columns) {
                result.setElementAtUnsafe(row, j, ring.add(matrix.elementAtUnsafe(0u, j), ring.ofInteger(convolution.elementAtUnsafe(i, j).roundToLong())))
            }
        }
//        println("maxRoundingError: $maxRoundingError")  //why not printed? In whiskered kronecker product every matrix multiplication is calling to subsequent multiplyTo() method
        return result
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<ModularUInt>): AbstractMatrix<ModularUInt> = coroutineScope {
        val mgList = list(rows) { i -> matrix.rowListAt(g.cachedPower(i).residue).map { FieldDouble.realComplexNumber(it.residue.toDouble()) } }
        val mgListRepeat = DftRepeatRowMatrix(FieldComplexNumberDouble, mgList, twoPower.value, matrix.columns)
        val convolution = dftInv.timesRowParallel(diag_dft_rgInvPadding.timesRowParallel(dft.timesRowParallel(mgListRepeat)))
        val result = ring.zeroMutableMatrix(rows, matrix.columns)
        for (i in 0u until rows) {
            launch {
                val row = g.cachedInversePower(i).residue - 1u
                for (j in 0u until matrix.columns) {
                    result.setElementAtUnsafe(row, j, ring.add(matrix.elementAtUnsafe(0u, j), ring.ofInteger(convolution.elementAtUnsafe(i, j).roundToLong())))
                }
            }
        }
        result
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<ModularUInt>, dest: AbstractMutableMatrix<ModularUInt>) {
        val mgList = list(rows) { i -> matrix.rowListAt(g.cachedPower(i).residue).map { FieldDouble.realComplexNumber(it.residue.toDouble()) } }
        val mgListRepeat = DftRepeatRowMatrix(FieldComplexNumberDouble, mgList, twoPower.value, matrix.columns)
        val convolution = dftInv.times(diag_dft_rgInvPadding.times(dft.times(mgListRepeat)))
        //        println(dft.times(mgListRepeat))
        //{5834201.0 + i 0.0, 5859970.0 + i 0.0},
        //{7445.408611825948 + i -4728.668483540232
        //        println(diag_dft_rgInvPadding.times(dft.times(mgListRepeat)))
        //{1.1673348810892E13 + i 0.0, 1.1503351627516E13 + i 0.0},
        //{3.384407836447184E9 + i -2.693198584837899E9
        //        println(dftInv.times(diag_dft_rgInvPadding.times(dft.times(mgListRepeat))))
        //{2.9147243770001636E9 + i -1.4645499754886396E-5      //for prime = 2879, it's not suffice to use Int to store values. Error term in (143, 0) is -0.11668586730957031

//        maxRoundingError = 0.0
        for (i in 0u until rows) {
            val row = g.cachedInversePower(i).residue - 1u
            for (j in 0u until matrix.columns) {
//                println("c($i,$j) : ${convolution.elementAtUnsafe(i, j)}")
                dest.setElementAtUnsafe(row, j, ring.add(matrix.elementAtUnsafe(0u, j), ring.ofInteger(convolution.elementAtUnsafe(i, j).roundToLong())))
            }
        }
//        println("maxRoundingError: $maxRoundingError")
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<ModularUInt>, dest: AbstractMutableMatrix<ModularUInt>) = coroutineScope {
        val mgList = list(rows) { i -> matrix.rowListAt(g.cachedPower(i).residue).map { FieldDouble.realComplexNumber(it.residue.toDouble()) } }
        val mgListRepeat = DftRepeatRowMatrix(FieldComplexNumberDouble, mgList, twoPower.value, matrix.columns)
        val convolution = dftInv.timesRowParallel(diag_dft_rgInvPadding.timesRowParallel(dft.timesRowParallel(mgListRepeat)))
        for (i in 0u until rows) {
            launch {
                val row = g.cachedInversePower(i).residue - 1u
                for (j in 0u until matrix.columns) {
                    dest.setElementAtUnsafe(row, j, ring.add(matrix.elementAtUnsafe(0u, j), ring.ofInteger(convolution.elementAtUnsafe(i, j).roundToLong())))
                }
            }
        }
    }

}