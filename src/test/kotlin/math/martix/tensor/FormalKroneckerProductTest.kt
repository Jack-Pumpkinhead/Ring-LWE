package math.martix.tensor

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.ringUInt
import math.andPrint
import math.martix.AbstractMatrix
import math.martix.constantMatrix
import math.martix.formalKroneckerProduct
import math.operations.multiply
import math.operations.multiplyRowParallel
import math.randomUIntMatrices
import math.randomUIntMatrix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/9 15:25
 */
internal class FormalKroneckerProductTest {


    @Test
    fun multiply() {
        repeat(100) {
            val m = randomUIntMatrices(5u, 1u..5u, 0u..10u)
            val m0 = m.fold(ringUInt.constantMatrix(1u) as AbstractMatrix<UInt>) { acc, i -> ringUInt.formalKroneckerProduct(acc, i) }
            val m1 = ringUInt.formalKroneckerProduct(m)
            val v = randomUIntMatrix(m0.columns..m0.columns, 1u..5u, 0u..10u).andPrint()
            val m0v = m0 * v
            val m1v = m1 * v
            val m0_v = multiply(m0, v)
            val m1_v = multiply(m1, v)

            for (matrix in m) {
                matrix.andPrint()
            }
            m0v.andPrint()
            assertEquals(m0v, m1v)
            assertEquals(m0v, m0_v)
            assertEquals(m1v, m1_v)
        }
    }

    @Test
    fun multiplyParallel() {
        runBlocking {
            repeat(100) {
                val m = randomUIntMatrices(5u, 1u..5u, 0u..10u)
                val m0 = m.fold(ringUInt.constantMatrix(1u) as AbstractMatrix<UInt>) { acc, i -> ringUInt.formalKroneckerProduct(acc, i) }
                val m1 = ringUInt.formalKroneckerProduct(m)
                val v = randomUIntMatrix(m0.columns..m0.columns, 1u..5u, 0u..10u).andPrint()
                val m0v = m0.timesRowParallel(v)
                val m1v = m1.timesRowParallel(v)
                val m0_v = multiplyRowParallel(m0, v)
                val m1_v = multiplyRowParallel(m1, v)

                for (matrix in m) {
                    matrix.andPrint()
                }
                m0v.andPrint()
                assertEquals(m0v, m1v)
                assertEquals(m0v, m0_v)
                assertEquals(m1v, m1_v)
            }
        }
    }

}