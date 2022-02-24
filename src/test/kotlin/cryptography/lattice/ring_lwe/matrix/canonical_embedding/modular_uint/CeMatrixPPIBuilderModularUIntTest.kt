package cryptography.lattice.ring_lwe.matrix.canonical_embedding.modular_uint

import cryptography.lattice.ring_lwe.ring.RootUIntPPI
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import kotlinx.coroutines.runBlocking
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.ulong.primeOf
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTwoMatrixMultiplicationTiming
import math.timing.TwoMatrix
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/24 17:26
 */
internal class CeMatrixPPIBuilderModularUIntTest {

    //slower than direct multiplication
    // *  : average 0.30747ms, deviation 1.23125ms
    // *p : average 0.44038ms, deviation 1.36598ms
    // * t: average 0.30218ms, deviation 1.25338ms
    // *pt: average 0.43338ms, deviation 1.40848ms
    //d*  : average 0.26015ms, deviation 1.18757ms
    //samples: 533, total time: 929.318656ms
    //range: 1..200
    @Test
    fun multiplication() {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            val range = 1u..200u
            for (i in range) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())

                val root = primeField.firstGenerator
                when (root) {
                    is RootUIntPPP -> {
                        for (subroot in root.allMaximalPrimePowerSubroot()) {
                            val ce = CeMatrixPPIBuilderModularUInt.build(subroot)
                            val x = primeField.randomMatrix(ce.columns, 2u)
                            statistic.go(TwoMatrix(ce, x))
                        }
                    }
                    is RootUIntPPI -> {
                        val ce = CeMatrixPPIBuilderModularUInt.build(root)
                        val x = primeField.randomMatrix(ce.columns, 2u)
                        statistic.go(TwoMatrix(ce, x))
                    }
                    else           -> error("unknown type of $root, class: ${root::class}")
                }
            }
            statistic.printAverageAndStandardDeviation()
            println("range: $range")
        }
    }
}