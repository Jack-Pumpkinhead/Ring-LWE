package cryptography.lattice.ring_lwe.matrix

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.RingUInt
import math.andPrint
import math.random.randomUIntMatrix
import math.statistic.MatrixTimingStatistic
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/27 17:39
 */
internal class LowerTriangularOneMatrixTest {

// *  : average 0.01346ms, deviation 0.01734ms
// *p : average 0.01138ms, deviation 0.01598ms
// * t: average 0.01230ms, deviation 0.10250ms
// *pt: average 0.00747ms, deviation 0.01460ms
//d*  : average 0.07565ms, deviation 0.16322ms
//d*p : average 0.18509ms, deviation 0.56697ms
//d* t: average 0.07713ms, deviation 0.15202ms
//d*pt: average 0.16001ms, deviation 0.28404ms
    @Test
    fun multiply() {
        runBlocking {
            repeat(1000) {
                val m = LowerTriangularOneMatrix(RingUInt, Random.nextUInt(1u..100u)).andPrint("m:")
                val x = Random.randomUIntMatrix(m.columns..m.columns, 1u..3u, 0u..100u).andPrint("x:")
                MatrixTimingStatistic.statistic.go(m to x)
            }
        }
        MatrixTimingStatistic.statistic.printAverageAndStandardDeviation()
    }
}