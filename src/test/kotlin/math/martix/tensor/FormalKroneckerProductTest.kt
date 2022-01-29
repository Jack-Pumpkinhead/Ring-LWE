package math.martix.tensor

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.RingUInt
import math.martix.AbstractMatrix
import math.martix.constantMatrix
import math.martix.formalKroneckerProduct
import math.random.randomUIntMatrices
import math.random.randomUIntMatrix
import math.statistic.RepeatTaskStatistic
import math.timing.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/1/9 15:25
 */
internal class FormalKroneckerProductTest {

    //    22s/0.6s
    @Test
    fun multiplication() {
        runBlocking {
            val statistic = RepeatTaskStatistic(
                ManyMatricesMultiplicationTiming<UInt>(
                    Task(" ((a*b)*c)") { m -> m.matrices[0] * m.matrices.last() },
                    Task("    a*b*c ") { m -> m.matrices[1] * m.matrices.last() },
                    Task("o((a*b)*c)") { m -> m.matrices[2] * m.matrices.last() },
//                Task("o   a*b*c ") { m -> m.matrices[3] * m.matrices.last() }
                )
            )
            repeat(100) {
                val m = Random.randomUIntMatrices(6u, 1u..5u, 0u..20u)
                val m0 = m.fold(RingUInt.constantMatrix(1u) as AbstractMatrix<UInt>) { acc, i -> RingUInt.formalKroneckerProduct(acc, i) }
                val m1 = RingUInt.formalKroneckerProduct(m)
                val m0o = m0.toOrdinaryMatrix()
//                val m1o = m1.toOrdinaryMatrix()
                val x = Random.randomUIntMatrix(m0.columns, 1u..2u, 0u..10u)
                statistic.go(ManyMatrices(listOf(m0, m1, m0o, x)))
//                statistic.go(ManyMatrices(listOf(m0, m1, m0o, m1o, x)))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

//    35.5s
    @Test
    fun multiplication1() {
        runBlocking {
            val statistic = RepeatTaskStatistic(TwoMatrixMultiplicationTiming<UInt>())
            repeat(100) {
                val m = Random.randomUIntMatrices(6u, 1u..5u, 0u..20u)
                val m0 = m.fold(RingUInt.constantMatrix(1u) as AbstractMatrix<UInt>) { acc, i -> RingUInt.formalKroneckerProduct(acc, i) }
                val x = Random.randomUIntMatrix(m0.columns, 1u..2u, 0u..10u)
                statistic.go(TwoMatrix(m0, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

//    7.5s
    @Test
    fun multiplication2() {
        runBlocking {
            val statistic = RepeatTaskStatistic(TwoMatrixMultiplicationTiming<UInt>())
            repeat(100) {
                val m = Random.randomUIntMatrices(6u, 1u..5u, 0u..20u)
                val m1 = RingUInt.formalKroneckerProduct(m)
                val x = Random.randomUIntMatrix(m1.columns, 1u..2u, 0u..10u)
                statistic.go(TwoMatrix(m1, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

}