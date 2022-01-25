package math.martix

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.RingUInt
import math.abstract_structure.instance.categoryUIntMatrix
import math.andPrint
import math.operation.*
import math.random.randomMultiplicableUIntMatrices
import math.random.randomUIntMatrix
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/1/19 22:01
 */
internal class FormalProductTest {

    @Test
    fun elementAt() {
        repeat(100) {
            val matrices = Random.randomMultiplicableUIntMatrices(5u, 1u..10u, 0u..100u)
            println("---------------")
            val matrix = FormalProduct(RingUInt, matrices).andPrint()
            val matrix1 = matrix.toOrdinaryMatrix().andPrint()
            val matrix2 = categoryUIntMatrix.composeAllPrefixedWithIdentity(matrices).andPrint()
            assertEquals(matrix, matrix1)
            assertEquals(matrix1, matrix2)
        }
    }

    @Test
    fun timesImpl() {
        repeat(1000) {
            val matrices = Random.randomMultiplicableUIntMatrices(3u, 1u..10u, 0u..100u)
            val matrix = FormalProduct(RingUInt, matrices)
            val matrix1 = matrix.toOrdinaryMatrix()
            val x = Random.randomUIntMatrix(matrices.last().columns..matrices.last().columns, 1u..10u, 0u..100u)
            val mx = (matrix * x).andPrint()
            val mx1 = multiply(matrix, x).andPrint()
            val m1x = matrix1 * x
            val m1x1 = multiply(matrix1, x)

            assertEquals(mx, mx1)
            assertEquals(m1x, m1x1)
            assertEquals(mx, m1x)
        }
    }

    @Test
    fun timesRowParallelImpl() {
        runBlocking {
            repeat(1000) {
                val matrices = Random.randomMultiplicableUIntMatrices(3u, 1u..10u, 0u..100u)
                val matrix = FormalProduct(RingUInt, matrices)
                val matrix1 = matrix.toOrdinaryMatrix()
                val x = Random.randomUIntMatrix(matrices.last().columns..matrices.last().columns, 1u..10u, 0u..100u)
                val mx = (matrix.timesRowParallel(x)).andPrint()
                val mx1 = multiplyRowParallel(matrix, x).andPrint()
                val m1x = (matrix1.timesRowParallel(x))
                val m1x1 = multiplyRowParallel(matrix1, x)

                assertEquals(mx, mx1)
                assertEquals(m1x, m1x1)
                assertEquals(mx, m1x)
            }
        }
    }

    @Test
    fun multiplyToImpl() {
        repeat(1000) {
            val matrices = Random.randomMultiplicableUIntMatrices(3u, 1u..10u, 0u..100u)
            val matrix = FormalProduct(RingUInt, matrices)
            val matrix1 = matrix.toOrdinaryMatrix()
            val x = Random.randomUIntMatrix(matrices.last().columns..matrices.last().columns, 1u..10u, 0u..100u)
            val mx = RingUInt.zeroMutableMatrix(matrices.first().rows, x.columns)
            val mx1 = RingUInt.zeroMutableMatrix(matrices.first().rows, x.columns)
            val m1x = RingUInt.zeroMutableMatrix(matrices.first().rows, x.columns)
            val m1x1 = RingUInt.zeroMutableMatrix(matrices.first().rows, x.columns)
            matrix.multiplyTo(x, mx)
            multiplyTo(matrix, x, mx1)
            matrix1.multiplyTo(x, m1x)
            multiplyTo(matrix1, x, m1x1)

            assertEquals(mx, mx1)
            assertEquals(m1x, m1x1)
            assertEquals(mx, m1x)
        }
    }

    @Test
    fun multiplyToRowParallelImpl() {
        runBlocking {
            repeat(1000) {
                val matrices = Random.randomMultiplicableUIntMatrices(3u, 1u..10u, 0u..100u)
                val matrix = FormalProduct(RingUInt, matrices)
                val matrix1 = matrix.toOrdinaryMatrix()
                val x = Random.randomUIntMatrix(matrices.last().columns..matrices.last().columns, 1u..10u, 0u..100u)
                val mx = RingUInt.zeroMutableMatrix(matrices.first().rows, x.columns)
                val mx1 = RingUInt.zeroMutableMatrix(matrices.first().rows, x.columns)
                val m1x = RingUInt.zeroMutableMatrix(matrices.first().rows, x.columns)
                val m1x1 = RingUInt.zeroMutableMatrix(matrices.first().rows, x.columns)
                matrix.multiplyToRowParallel(x, mx)
                multiplyToRowParallel(matrix, x, mx1)
                matrix1.multiplyToRowParallel(x, m1x)
                multiplyToRowParallel(matrix1, x, m1x1)

                assertEquals(mx, mx1)
                assertEquals(m1x, m1x1)
                assertEquals(mx, m1x)
            }
        }
    }
}