package math.abstract_structure

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.categoryUIntMatrix
import math.integer.uint.RingUInt
import math.martix.FormalProduct
import math.martix.identityMatrix
import math.operation.composeAll
import math.operation.composeAllPrefixedWithIdentity
import math.random.randomMultiplicableUIntMatrices
import math.statistic.TaskTimingStatistic
import math.timing.EqualManyMatricesMultiplicationTiming
import math.timing.ManyMatrices
import math.timing.Task
import org.junit.jupiter.api.Test
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/1/8 13:32
 */
internal class CategoryInstanceKtTest {

    //  assertion in FormalProduct may costly
    //  multiplication by FormalProduct is a bit slower, but more time-stable

    //c : average 61.95125ms, deviation 5.36783ms
    //ci: average 62.20267ms, deviation 4.01629ms
    //FP: average 70.41571ms, deviation 3.58413ms
    //total: 19.456962800s

    //c : average 68.17331ms, deviation 5.03362ms
    //ci: average 68.42159ms, deviation 3.42225ms
    //FP: average 74.79191ms, deviation 2.94362ms
    //total: 21.138681300s
    @Test
    fun multiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(
                EqualManyMatricesMultiplicationTiming<UInt>(
                    Task("c ") { m -> categoryUIntMatrix.composeAll(m.matrices) },
                    Task("ci") { m -> categoryUIntMatrix.composeAllPrefixedWithIdentity(m.matrices) },
                    Task("FP") { m -> FormalProduct(RingUInt, m.matrices.first().rows, m.matrices.last().columns, m.matrices) * RingUInt.identityMatrix(m.matrices.last().columns) }
                )
            )
            for (i in 1u..100u) {
                val m = Random.randomMultiplicableUIntMatrices(50u, 50u..55u, 0u..100u)
                statistic.go(ManyMatrices(m))
            }
            statistic.printAverageAndStandardDeviation()
        }
    }
}