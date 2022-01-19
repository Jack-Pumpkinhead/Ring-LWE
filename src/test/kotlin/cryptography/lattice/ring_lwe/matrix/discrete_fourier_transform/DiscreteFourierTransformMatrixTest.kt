package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.concrete.primeFieldDFT
import kotlinx.coroutines.runBlocking
import math.andPrint
import math.integer.modular.FieldULongModular
import math.integer.primeFactorization1
import math.integer.primeOf
import math.operation.multiply
import math.random.randomModularULongMatrix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/1/19 21:31
 */
internal class DiscreteFourierTransformMatrixTest {

    @Test
    fun primeField() {
        runBlocking {

            for (i in 1u..11u) {
                val primeModulus = primeOf(i)
                val factorization = primeFactorization1(primeModulus - 1uL)
                val dft = primeFieldDFT(primeModulus)
                val x = Random.randomModularULongMatrix(FieldULongModular(primeModulus), primeModulus, dft.columns..dft.columns, 1u..3u)
                println("-------primeModulus: $primeModulus, $factorization----------------")
                dft.andPrint()
                dft.underlyingMatrix.andPrint()
                assertEquals(dft, dft.underlyingMatrix)

                val a = (dft * x).andPrint()
                val a1 = multiply(dft, x).andPrint()
                assertEquals(a, a1)
                println("-----------------------")
            }

        }
    }

}