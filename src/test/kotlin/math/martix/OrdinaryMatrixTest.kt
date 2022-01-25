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
 * Created by CowardlyLion at 2022/1/9 17:48
 */
internal class OrdinaryMatrixTest {

    @Test
    fun identityMatrix() {
        repeat(1000) {
            val m = Random.randomUIntMatrix(1u..100u, 1u..100u, 0u..100u).andPrint()
            val ml = RingUInt.identityOrdinaryMatrix(m.rows).andPrint()
            val mr = RingUInt.identityOrdinaryMatrix(m.columns).andPrint()
            val m1 = categoryUIntMatrix.composeAllPrefixedWithIdentity(listOf(ml, m)).andPrint()
            val m2 = categoryUIntMatrix.composeAllPrefixedWithIdentity(listOf(m, mr)).andPrint()
//            val m1 = (ml * m).andPrint()
//            val m2 = (m * mr).andPrint()
            assertEquals(m, m1)
            assertEquals(m, m2)
        }
    }

    @Test
    fun timesImpl() {
        //make sure optimized-multiplication agree with standard multiplication
        repeat(1000) {
            val m = Random.randomMultiplicableUIntMatrices(2u, 1u..100u, 0u..100u)
            m[0].andPrint()
            m[1].andPrint()
            val m1 = categoryUIntMatrix.composeAllPrefixedWithIdentity(m).andPrint()
            val m2 = multiply(m[0], m[1]).andPrint()
            assertEquals(m1, m2)
        }
    }

    @Test
    fun timesRowParallelImpl() {
        runBlocking {
            repeat(1000) {
                val m = Random.randomMultiplicableUIntMatrices(2u, 1u..100u, 0u..100u)
                m[0].andPrint()
                m[1].andPrint()
                val m1 = (m[0].timesRowParallel(m[1])).andPrint()
                val m2 = multiplyRowParallel(m[0], m[1]).andPrint()
                assertEquals(m1, m2)
            }
        }
    }


    @Test
    fun multiplyToImpl() {
        repeat(1000) {
            val m = Random.randomMultiplicableUIntMatrices(2u, 1u..100u, 0u..100u)
            m[0].andPrint()
            m[1].andPrint()
            val m0 = (m[0] * m[1]).andPrint()
            val m01 = RingUInt.zeroMutableMatrix(m[0].rows, m[1].columns)
            val m02 = RingUInt.zeroMutableMatrix(m[0].rows, m[1].columns)
            m[0].multiplyTo(m[1], m01)
            multiplyTo(m[0], m[1], m02)
            assertEquals(m0, m01)
            assertEquals(m0, m02)
        }
    }

    @Test
    fun multiplyToParallelImpl() {
        runBlocking {
            repeat(1000) {
                val m = Random.randomMultiplicableUIntMatrices(2u, 1u..100u, 0u..100u)
                m[0].andPrint()
                m[1].andPrint()
                val m0 = (m[0] * m[1]).andPrint()
                val m01 = RingUInt.zeroMutableMatrix(m[0].rows, m[1].columns)
                val m02 = RingUInt.zeroMutableMatrix(m[0].rows, m[1].columns)
                m[0].multiplyToRowParallel(m[1], m01)
                multiplyToRowParallel(m[0], m[1], m02)
                assertEquals(m0, m01)
                assertEquals(m0, m02)
            }
        }
    }
}