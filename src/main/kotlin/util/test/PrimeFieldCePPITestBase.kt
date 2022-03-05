package util.test

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.CeMatrixBuilder
import cryptography.lattice.ring_lwe.ring.RootPPI
import cryptography.lattice.ring_lwe.ring.RootPPP
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafeModularUInt
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.ulong.primeOf
import math.martix.AbstractMatrix
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTwoMatrixMultiplicationTiming
import math.timing.TwoMatrix
import util.errorUnknownObject

/**
 * Created by CowardlyLion at 2022/3/5 13:48
 */
class PrimeFieldCePPITestBase(val builder: CeMatrixBuilder<ModularUInt>, val statistic: TaskTimingStatistic<TwoMatrix<ModularUInt>, AbstractMatrix<ModularUInt>> = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming())) {

    suspend fun test(range: UIntRange, repeat: UInt = 1u) {
        for (i in range) {
            val primeField = FieldModularUInt(primeOf(i).toUInt())
            val root = primeField.firstGenerator
            when (root) {
                is RootPPP -> {
                    for (subroot in SubrootCalculatorUnsafeModularUInt.allMaximalPrimePowerSubroot(root)) {
                        val ce = builder.compute(subroot)
                        util.stdlib.repeat(repeat) {
                            val x = primeField.randomMatrix(ce.columns, 2u)
                            statistic.go(TwoMatrix(ce, x))
                        }
                    }
                }
                is RootPPI -> {
                    val ce = builder.compute(root)
                    util.stdlib.repeat(repeat) {
                        val x = primeField.randomMatrix(ce.columns, 2u)
                        statistic.go(TwoMatrix(ce, x))
                    }
                }
                else       -> errorUnknownObject(root)
            }
        }
        statistic.printAverageAndStandardDeviation()
        println("range: $range")
    }

}