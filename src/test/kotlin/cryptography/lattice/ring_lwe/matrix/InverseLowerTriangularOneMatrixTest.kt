package cryptography.lattice.ring_lwe.matrix

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.RingUInt
import math.andPrint
import math.random.randomUIntMatrix
import math.statistic.RepeatTaskStatistic
import math.timing.TwoMatrix
import math.timing.TwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/27 21:50
 */
internal class InverseLowerTriangularOneMatrixTest {

    @Test
    fun multiply() {
        runBlocking {
            val statistic = RepeatTaskStatistic(TwoMatrixMultiplicationTiming<UInt>())
            repeat(1000) {
                val m = InverseLowerTriangularOneMatrix(RingUInt, Random.nextUInt(1u..100u)).andPrint("m:")
                val x = Random.randomUIntMatrix(m.columns..m.columns, 1u..3u, 0u..100u).andPrint("x:")
                statistic.go(TwoMatrix(m, x))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }
}