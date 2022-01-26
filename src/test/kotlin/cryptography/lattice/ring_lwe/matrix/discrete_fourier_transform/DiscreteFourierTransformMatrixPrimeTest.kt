package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import kotlinx.coroutines.runBlocking
import math.andPrint
import math.integer.firstMultiplicativeGeneratorOfPrimeFieldUnsafe
import math.abstract_structure.instance.FieldModularULong
import math.integer.modular.ULongModular
import math.integer.primeFactorization
import math.integer.primeFactorization1
import math.integer.primeOf
import math.operation.multiply
import math.random.randomModularULongMatrix
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/19 23:13
 */
internal class DiscreteFourierTransformMatrixPrimeTest {

//    TODO improve performance(implement real dft) 16s 19s
    @Test
    fun primeField() {
        runBlocking {
            for (i in 1u..250u) {
                val primeModulus = primeOf(i)
                val order = primeModulus - 1uL
                val factorization = primeFactorization1(order)
                val root = firstMultiplicativeGeneratorOfPrimeFieldUnsafe(primeModulus)
                val field = FieldModularULong(primeModulus)
                val rootData = RootData(field, ULongModular(primeModulus, root), order, order.primeFactorization())
                val dft = DiscreteFourierTransformMatrixPrime(rootData.subRootDataPrime(Random.nextUInt(rootData.orderFactorization.size.toUInt())))
                val x = Random.randomModularULongMatrix(field, primeModulus, dft.columns..dft.columns, 1u..3u)
//                println("-------primeModulus: $primeModulus, $factorization----------------")
                dft.andPrint()
                x.andPrint()
                val m = (dft * x).andPrint()
                val m1 = multiply(dft, x).andPrint()
                assertEquals(m, m1)
//                println("-----------------------")
            }
        }
    }

}