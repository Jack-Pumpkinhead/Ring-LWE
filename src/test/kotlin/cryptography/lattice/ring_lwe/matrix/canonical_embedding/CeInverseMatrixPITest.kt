package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.modular_uint.CeMatrixPPIBuilderModularUInt
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
 * Created by CowardlyLion at 2022/2/27 21:22
 */
internal class CeInverseMatrixPITest {


    // *  : average 0.22648ms, deviation 0.97380ms
    // *p : average 0.33078ms, deviation 1.00609ms
    // * t: average 0.20186ms, deviation 0.88021ms
    // *pt: average 0.31954ms, deviation 0.95257ms
    //d*  : average 77.36159ms, deviation 776.20488ms
    //samples: 533, total time: 41.808656s
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
                            statistic.go(TwoMatrix(ce.inverse, x))
                        }
                    }
                    is RootUIntPPI -> {
                        val ce = CeMatrixPPIBuilderModularUInt.build(root)
                        val x = primeField.randomMatrix(ce.columns, 2u)
                        statistic.go(TwoMatrix(ce.inverse, x))
                    }
                    else           -> error("unknown type of $root, class: ${root::class}")
                }
            }
            statistic.printAverageAndStandardDeviation()
            println("range: $range")
        }
    }

}