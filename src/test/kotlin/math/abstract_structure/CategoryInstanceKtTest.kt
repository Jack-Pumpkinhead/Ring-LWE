package math.abstract_structure

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.RingUInt
import math.abstract_structure.instance.categoryUIntMatrix
import math.martix.FormalProduct
import math.martix.identityMatrix
import math.operation.composeAll
import math.operation.composeAllPrefixedWithIdentity
import math.random.randomMultiplicableUIntMatrices
import math.statistic.TaskTimingStatistic
import math.timing.ManyMatrices
import math.timing.EqualManyMatricesMultiplicationTiming
import math.timing.Task
import org.junit.jupiter.api.Test
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/1/8 13:32
 */
internal class CategoryInstanceKtTest {

    //  21.4s  21.3s
    //  21.2s 21.2s (disable assertion)
    //  assertion in FormalProduct is costly (but FormalProduct is slower even if disable assertion)
    //  TODO find out why multiplication by FormalProduct is slower
    @Test
    fun multiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(
                EqualManyMatricesMultiplicationTiming<UInt>(
                    Task("c ") { m -> categoryUIntMatrix.composeAll(m.matrices) },
                    Task("ci") { m -> categoryUIntMatrix.composeAllPrefixedWithIdentity(m.matrices) },
                    Task("d ") { m -> FormalProduct(RingUInt, m.matrices) * RingUInt.identityMatrix(m.matrices.last().columns) }
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