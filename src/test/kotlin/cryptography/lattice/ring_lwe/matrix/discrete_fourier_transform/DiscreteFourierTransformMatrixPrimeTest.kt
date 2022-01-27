package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.matrix.RootDataUInt
import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.PrimeFieldUInt
import math.andPrint
import math.integer.firstMultiplicativeGeneratorOfPrimeFieldUnsafe
import math.integer.modular.ModularUInt
import math.integer.primeFactorization
import math.integer.primeOf
import math.operation.multiply
import math.random.randomModularUIntMatrix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/19 23:13
 */
internal class DiscreteFourierTransformMatrixPrimeTest {

    //    TODO improve performance(implement real dft) 16s 19s (1sec 235ms??? what made it slow?)
//   i:[1,700]  17s  18s  23s  18s
    @Test
    fun primeField() {
        runBlocking {
            for (i in 1u..700u) {
                val primeField = PrimeFieldUInt(primeOf(i).toUInt())
                val order = primeField.prime - 1u
                val root = firstMultiplicativeGeneratorOfPrimeFieldUnsafe(primeField.prime)
                val rootData = RootDataUInt(primeField, ModularUInt(primeField.prime, root), order.primeFactorization())
                val dft = DiscreteFourierTransformMatrixPrime(rootData.primeSubroot(Random.nextUInt(rootData.order.factors.size.toUInt())))
                val x = primeField.randomModularUIntMatrix(dft.columns..dft.columns, 1u..3u)
//                println("-------primeModulus: $primeModulus, $factorization----------------")
                dft.andPrint("dft:")
                x.andPrint("x:")
//                println("$i\tsize: ${dft.rows}")
                val m = (dft * x).andPrint("m:")
                val m1 = multiply(dft, x).andPrint("m1:")
                assertEquals(m, m1)
//                println("-----------------------")
            }
        }
    }

}