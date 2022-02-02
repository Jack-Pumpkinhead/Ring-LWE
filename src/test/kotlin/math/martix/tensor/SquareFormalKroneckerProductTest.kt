package math.martix.tensor

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.RingUInt
import math.martix.AbstractSquareMatrix
import math.martix.constantMatrix
import math.martix.formalKroneckerProduct
import math.random.randomSquareUIntMatrices
import math.random.randomUIntMatrix
import math.statistic.TaskTimingStatistic
import math.timing.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/1/29 12:54
 */
internal class SquareFormalKroneckerProductTest {

    @Test
    fun multiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(
                ManyMatricesMultiplicationTiming<UInt>(
                    Task(" ((a*b)*c)") { m -> m.matrices[0] * m.matrices.last() },
                    Task("    a*b*c ") { m -> m.matrices[1] * m.matrices.last() },
                    Task("o((a*b)*c)") { m -> m.matrices[2] * m.matrices.last() },
//                Task("o   a*b*c ") { m -> m.matrices[3] * m.matrices.last() }
                )
            )
            repeat(100) {
                val m = Random.randomSquareUIntMatrices(5u, 1u..4u, 0u..20u)
                val m0 = m.fold(RingUInt.constantMatrix(1u) as AbstractSquareMatrix<UInt>) { acc, i -> RingUInt.formalKroneckerProduct(acc, i) }
                val m1: SquareFormalKroneckerProduct<UInt> = RingUInt.formalKroneckerProduct(m)
                val m0o = m0.toOrdinaryMatrix()
//                val m1o = m1.toOrdinaryMatrix()
                val x = Random.randomUIntMatrix(m0.columns, 1u..2u, 0u..10u)
                statistic.go(ManyMatrices(listOf(m0, m1, m0o, x)))
//                statistic.go(ManyMatrices(listOf(m0, m1, m0o, m1o, x)))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    @Test
    fun multiplication1() {
        runBlocking {
            val statistic = TaskTimingStatistic(TwoMatrixMultiplicationTiming<UInt>())
            repeat(100) {
                val m = Random.randomSquareUIntMatrices(5u, 1u..4u, 0u..20u)
                val m0 = m.fold(RingUInt.constantMatrix(1u) as AbstractSquareMatrix<UInt>) { acc, i -> RingUInt.formalKroneckerProduct(acc, i) }
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
            val statistic = TaskTimingStatistic(TwoMatrixMultiplicationTiming<UInt>())
            repeat(100) {
                val m = Random.randomSquareUIntMatrices(5u, 1u..4u, 0u..20u)
                val m1: SquareFormalKroneckerProduct<UInt> = RingUInt.formalKroneckerProduct(m)
                val x = Random.randomUIntMatrix(m1.columns, 1u..2u, 0u..10u)
                statistic.go(TwoMatrix(m1, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

}