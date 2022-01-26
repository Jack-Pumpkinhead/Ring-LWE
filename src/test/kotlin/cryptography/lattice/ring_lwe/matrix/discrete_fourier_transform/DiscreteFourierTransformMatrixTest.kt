package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete.dft
import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.PrimeFieldUInt
import math.andPrint
import math.integer.primeOf
import math.martix.zeroMutableMatrix
import math.operation.multiply
import math.operation.multiplyRowParallel
import math.operation.multiplyTo
import math.operation.multiplyToRowParallel
import math.random.randomModularUIntMatrix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/19 21:31
 */
internal class DiscreteFourierTransformMatrixTest {

    @Test
    fun timesImpl() {
        runBlocking {
            for (i in 1u..70u) {
                val primeField = PrimeFieldUInt(primeOf(i).toUInt())
                val dft = primeField.dft()
                val x = primeField.randomModularUIntMatrix(dft.columns..dft.columns, 1u..3u)
//                println("-------primeModulus: $primeModulus, $factorization----------------")
//                dft.andPrint()
//                dft.underlyingMatrix.andPrint()
//                assertEquals(dft, dft.underlyingMatrix)

                val a = (dft * x).andPrint()
                val a1 = multiply(dft, x).andPrint()
                assertEquals(a, a1)
//                println("-----------------------")
            }

        }
    }

    @Test
    fun timesRowParallelImpl() {
        runBlocking {
            for (i in 1u..70u) {
                val primeField = PrimeFieldUInt(primeOf(i).toUInt())
                val dft = primeField.dft()
                val x = primeField.randomModularUIntMatrix(dft.columns..dft.columns, 1u..3u)
//                println("-------primeModulus: $primeModulus, $factorization----------------")

                val a = (dft.timesRowParallel(x)).andPrint()
                val a1 = multiplyRowParallel(dft, x).andPrint()
                assertEquals(a, a1)
//                println("-----------------------")
            }
        }
    }

    @Test
    fun multiplyToImpl() {
        runBlocking {
            for (i in 1u..70u) {
                val primeField = PrimeFieldUInt(primeOf(i).toUInt())
                val dft = primeField.dft()
                val x = primeField.randomModularUIntMatrix(dft.columns..dft.columns, 1u..3u)
//                println("-------primeModulus: $primeModulus, $factorization----------------")

                val a = primeField.zeroMutableMatrix(dft.rows, x.columns)
                val a1 = primeField.zeroMutableMatrix(dft.rows, x.columns)
                dft.multiplyToImpl(x, a)
                multiplyTo(dft, x, a1)
                assertEquals(a, a1)
//                println("-----------------------")
            }
        }
    }

    @Test
    fun multiplyToRowParallelImpl() {
        runBlocking {
            for (i in 1u..70u) {
                val primeField = PrimeFieldUInt(primeOf(i).toUInt())
                val dft = primeField.dft()
                val x = primeField.randomModularUIntMatrix(dft.columns..dft.columns, 1u..3u)
//                println("-------primeModulus: $primeModulus, $factorization----------------")

                val a = primeField.zeroMutableMatrix(dft.rows, x.columns)
                val a1 = primeField.zeroMutableMatrix(dft.rows, x.columns)
                dft.multiplyToRowParallelImpl(x, a)
                multiplyToRowParallel(dft, x, a1)
                assertEquals(a, a1)
//                println("-----------------------")
            }
        }
    }

}