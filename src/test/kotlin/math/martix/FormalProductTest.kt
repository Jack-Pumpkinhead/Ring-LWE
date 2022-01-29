package math.martix

import math.abstract_structure.instance.RingUInt
import math.abstract_structure.instance.categoryUIntMatrix
import math.andPrint
import math.operation.composeAllPrefixedWithIdentity
import math.operation.matrixEquals
import math.random.randomMultiplicableUIntMatrices
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/1/19 22:01
 */
internal class FormalProductTest {

    @Test
    fun elementAt() {
        repeat(100) {
            val matrices = Random.randomMultiplicableUIntMatrices(5u, 1u..10u, 0u..100u)
//            println("---------------")
            val matrix = FormalProduct(RingUInt, matrices).andPrint()
            val matrix1 = matrix.toOrdinaryMatrix().andPrint()
            val matrix2 = categoryUIntMatrix.composeAllPrefixedWithIdentity(matrices).andPrint()
            assertTrue(matrixEquals(matrix, matrix1))
            assertTrue(matrixEquals(matrix1, matrix2))
        }
    }

//    for tests about multiplication, see CategoryInstanceKtTest

}