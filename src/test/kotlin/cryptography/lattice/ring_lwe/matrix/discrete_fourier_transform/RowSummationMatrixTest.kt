package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.RingUInt
import math.andPrint
import math.random.randomUIntMatrix
import math.statistic.TaskTimingStatistic
import math.timing.TwoMatrix
import math.timing.EqualTwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/29 21:15
 */
internal class RowSummationMatrixTest{

    @Test
    fun multiply() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<UInt>())
            repeat(1000) {
                val m = RowSummationMatrix(RingUInt, Random.nextUInt(1u..100u)).andPrint("m:")
                val x = Random.randomUIntMatrix(m.columns, 2u, 0u..100u).andPrint("x:")
                statistic.go(TwoMatrix(m, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }
}