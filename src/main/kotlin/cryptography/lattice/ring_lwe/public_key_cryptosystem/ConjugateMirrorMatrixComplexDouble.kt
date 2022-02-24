package cryptography.lattice.ring_lwe.public_key_cryptosystem

import math.abstract_structure.Ring
import math.abstract_structure.instance.FieldComplexNumberDouble
import math.abstract_structure.instance.FieldDouble
import math.complex_number.ComplexNumber
import math.complex_number.imaginaryComplexNumber
import math.complex_number.realComplexNumber
import math.halfSqrtTwo
import math.martix.AbstractMatrix
import math.martix.AbstractSquareMatrix
import math.martix.mutable.AbstractMutableMatrix
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/2/24 20:44
 *
 * a unitary basis of image of canonical embedding in C^φ(n)
 */
class ConjugateMirrorMatrixComplexDouble(override val size: UInt, val halfSize: UInt = size / 2u) : AbstractSquareMatrix<ComplexNumber<Double>> {

    init {
        lazyAssert2 {
            assert(halfSize * 2u == size)
        }
    }

    companion object {
        val halfSqrtTwo1 = FieldDouble.realComplexNumber(halfSqrtTwo)
        val halfSqrtTwoI = FieldDouble.imaginaryComplexNumber(halfSqrtTwo)
        val negHalfSqrtTwoI = FieldDouble.imaginaryComplexNumber(-halfSqrtTwo)
    }

    override val ring: Ring<ComplexNumber<Double>> get() = FieldComplexNumberDouble

    override fun elementAtUnsafe(row: UInt, column: UInt): ComplexNumber<Double> =
        if (row == column) {
            if (row < halfSize) {
                halfSqrtTwo1
            } else {
                negHalfSqrtTwoI
            }
        } else if (row + column + 1u == size) {
            if (column < halfSize) {
                halfSqrtTwo1
            } else {
                halfSqrtTwoI
            }
        } else ring.zero

    override fun determinant(): ComplexNumber<Double> {
        TODO("Not yet implemented")
    }

    override val inverse: AbstractSquareMatrix<ComplexNumber<Double>>
        get() = TODO("Not yet implemented")


    override fun timesImpl(matrix: AbstractMatrix<ComplexNumber<Double>>): AbstractMatrix<ComplexNumber<Double>> {
        return super.timesImpl(matrix)
    }

    override suspend fun timesRowParallelImpl(matrix: AbstractMatrix<ComplexNumber<Double>>): AbstractMatrix<ComplexNumber<Double>> {
        return super.timesRowParallelImpl(matrix)
    }

    override fun multiplyToImpl(matrix: AbstractMatrix<ComplexNumber<Double>>, dest: AbstractMutableMatrix<ComplexNumber<Double>>) {
        super.multiplyToImpl(matrix, dest)
    }

    override suspend fun multiplyToRowParallelImpl(matrix: AbstractMatrix<ComplexNumber<Double>>, dest: AbstractMutableMatrix<ComplexNumber<Double>>) {
        super.multiplyToRowParallelImpl(matrix, dest)
    }
}