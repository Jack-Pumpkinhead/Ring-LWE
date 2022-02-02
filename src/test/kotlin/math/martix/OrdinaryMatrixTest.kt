package math.martix

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.RingUInt
import math.andPrint
import math.random.randomMultiplicableUIntMatrices
import math.random.randomUIntMatrix
import math.statistic.TaskTimingStatistic
import math.timing.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/1/9 17:48
 */
internal class OrdinaryMatrixTest {

    @Test
    fun identityMatrix() {
        runBlocking {
            val statistic = TaskTimingStatistic(
                ThreeMatrixMultiplicationTiming<UInt>(
                    Task("     m") { (_, m, _) -> m },
                    Task("id * m") { (a, m, _) -> a * m },
                    Task("m * id") { (_, m, b) -> m * b },
                )
            )
            repeat(1000) {
                val m = Random.randomUIntMatrix(1u..100u, 1u..100u, 0u..100u).andPrint()
                val a = RingUInt.identityOrdinaryMatrix(m.rows).andPrint()
                val b = RingUInt.identityOrdinaryMatrix(m.columns).andPrint()
                statistic.go(ThreeMatrix(a, m, b))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

    @Test
    fun multiplication() {
        //make sure optimized-multiplication agree with standard multiplication
        runBlocking {
            val statistic = TaskTimingStatistic(TwoMatrixMultiplicationTiming<UInt>())
            repeat(1000) {
                val m = Random.randomMultiplicableUIntMatrices(2u, 1u..100u, 0u..100u)
                statistic.go(TwoMatrix(m[0], m[1]))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

//    total 32s
    @Test
    fun largeMultiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(TwoMatrixMultiplicationTiming<UInt>())
            repeat(50) {
                val m = Random.randomMultiplicableUIntMatrices(2u, 200u..210u, 0u..100u)
                statistic.go(TwoMatrix(m[0], m[1]))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }

}