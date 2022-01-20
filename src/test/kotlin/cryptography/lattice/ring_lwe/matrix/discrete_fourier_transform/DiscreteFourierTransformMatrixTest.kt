package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete.primeFieldDFT
import kotlinx.coroutines.runBlocking
import math.andPrint
import math.integer.modular.FieldULongModular
import math.integer.primeFactorization1
import math.integer.primeOf
import math.martix.zeroMutableMatrix
import math.operation.multiply
import math.operation.multiplyRowParallel
import math.operation.multiplyTo
import math.operation.multiplyToRowParallel
import math.random.randomModularULongMatrix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/1/19 21:31
 */
internal class DiscreteFourierTransformMatrixTest {

    @Test
    fun timesImpl() {
        runBlocking {
            for (i in 1u..70u) {
                val primeModulus = primeOf(i)
//                println("i: $i, primeModulus: $primeModulus")
                val factorization = primeFactorization1(primeModulus - 1uL)
                val dft = primeFieldDFT(primeModulus)
                val x = Random.randomModularULongMatrix(FieldULongModular(primeModulus), primeModulus, dft.columns..dft.columns, 1u..3u)
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
                val primeModulus = primeOf(i)
                val factorization = primeFactorization1(primeModulus - 1uL)
                val dft = primeFieldDFT(primeModulus)
                val x = Random.randomModularULongMatrix(FieldULongModular(primeModulus), primeModulus, dft.columns..dft.columns, 1u..3u)
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
                val primeModulus = primeOf(i)
                val factorization = primeFactorization1(primeModulus - 1uL)
                val dft = primeFieldDFT(primeModulus)
                val field = FieldULongModular(primeModulus)
                val x = Random.randomModularULongMatrix(field, primeModulus, dft.columns..dft.columns, 1u..3u)
//                println("-------primeModulus: $primeModulus, $factorization----------------")

                val a = field.zeroMutableMatrix(dft.rows, x.columns)
                val a1 = field.zeroMutableMatrix(dft.rows, x.columns)
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
                val primeModulus = primeOf(i)
                val factorization = primeFactorization1(primeModulus - 1uL)
                val dft = primeFieldDFT(primeModulus)
                val field = FieldULongModular(primeModulus)
                val x = Random.randomModularULongMatrix(field, primeModulus, dft.columns..dft.columns, 1u..3u)
//                println("-------primeModulus: $primeModulus, $factorization----------------")

                val a = field.zeroMutableMatrix(dft.rows, x.columns)
                val a1 = field.zeroMutableMatrix(dft.rows, x.columns)
                dft.multiplyToRowParallelImpl(x, a)
                multiplyToRowParallel(dft, x, a1)
                assertEquals(a, a1)
//                println("-----------------------")
            }
        }
    }

}