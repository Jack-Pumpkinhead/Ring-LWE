package math.martix.tensor

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.RingUInt
import math.andPrint
import math.martix.AbstractMatrix
import math.martix.concrete.OrdinaryMatrix
import math.martix.constantMatrix
import math.martix.formalKroneckerProduct
import math.measureTimeAndPrint
import math.operation.multiply
import math.operation.multiplyRowParallel
import math.random.randomUIntMatrices
import math.random.randomUIntMatrix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.time.ExperimentalTime

/**
 * Created by CowardlyLion at 2022/1/9 15:25
 */
internal class FormalKroneckerProductTest {


    @Test
    fun multiply() {
        repeat(100) {
            val m = Random.randomUIntMatrices(5u, 1u..5u, 0u..10u)
            val m0 = m.fold(RingUInt.constantMatrix(1u) as AbstractMatrix<UInt>) { acc, i -> RingUInt.formalKroneckerProduct(acc, i) }
            val m1 = RingUInt.formalKroneckerProduct(m)
            val v = Random.randomUIntMatrix(m0.columns..m0.columns, 1u..5u, 0u..10u).andPrint()
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
                val m = Random.randomUIntMatrices(5u, 1u..5u, 0u..10u)
                val m0 = m.fold(RingUInt.constantMatrix(1u) as AbstractMatrix<UInt>) { acc, i -> RingUInt.formalKroneckerProduct(acc, i) }
                val m1 = RingUInt.formalKroneckerProduct(m)
                val v = Random.randomUIntMatrix(m0.columns..m0.columns, 1u..5u, 0u..10u).andPrint()
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

    data class Sample(val m0: AbstractMatrix<UInt>, val m1: FormalKroneckerProduct<UInt>, val m2: OrdinaryMatrix<UInt>, val v: OrdinaryMatrix<UInt>)

    /**
     * time0: 64.48300ms
     * time0p: 117.77890ms
     * time0b: 6010.34660ms
     * time0bp: 6139.14530ms
     *
     * time1: 17.96020ms
     * time1p: 32.36480ms
     * time1b: 2430.95590ms
     * time1bp: 2350.64290ms
     *
     * time2: 181.30520ms
     * time2p: 217.21610ms
     * time2b: 169.99130ms
     * time2bp: 207.89660ms
     *
     * This performance test shows:
     *      naive parallel algorithm offers no improvement on performance (possibly due to overhead of threading)
     * */
    @OptIn(ExperimentalTime::class)
    @Test
    fun performance() {
        runBlocking {
            val samples = List(100) {
                val m = Random.randomUIntMatrices(5u, 1u..5u, 0u..20u)
                val m0 = m.fold(RingUInt.constantMatrix(1u) as AbstractMatrix<UInt>) { acc, i -> RingUInt.formalKroneckerProduct(acc, i) }
                val m1 = RingUInt.formalKroneckerProduct(m)
                val m2 = m1.toOrdinaryMatrix()
                val v = Random.randomUIntMatrix(m0.columns..m0.columns, 1u..5u, 0u..10u)
                Sample(m0, m1, m2, v)
            }

            measureTimeAndPrint("time0") { for ((m0, _, _, v) in samples) m0 * v }
            measureTimeAndPrint("time0p") { for ((m0, _, _, v) in samples) m0.timesRowParallel(v) }
            measureTimeAndPrint("time0b") { for ((m0, _, _, v) in samples) multiply(m0, v) }
            measureTimeAndPrint("time0bp") { for ((m0, _, _, v) in samples) multiplyRowParallel(m0, v) }
            println()
            measureTimeAndPrint("time1") { for ((_, m1, _, v) in samples) m1 * v }
            measureTimeAndPrint("time1p") { for ((_, m1, _, v) in samples) m1.timesRowParallel(v) }
            measureTimeAndPrint("time1b") { for ((_, m1, _, v) in samples) multiply(m1, v) }
            measureTimeAndPrint("time1bp") { for ((_, m1, _, v) in samples) multiplyRowParallel(m1, v) }
            println()
            measureTimeAndPrint("time2") { for ((_, _, m2, v) in samples) m2 * v }
            measureTimeAndPrint("time2p") { for ((_, _, m2, v) in samples) m2.timesRowParallel(v) }
            measureTimeAndPrint("time2b") { for ((_, _, m2, v) in samples) multiply(m2, v) }
            measureTimeAndPrint("time2bp") { for ((_, _, m2, v) in samples) multiplyRowParallel(m2, v) }

        }
    }
}