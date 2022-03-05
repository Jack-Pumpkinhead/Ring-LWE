package util.test

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint.DftMatrixBuilderModularUInt
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.ulong.primeOf
import math.martix.AbstractMatrix
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTwoMatrixMultiplicationTiming
import math.timing.TwoMatrix

/**
 * Created by CowardlyLion at 2022/3/5 12:46
 */
class FullPrimeFieldDftTestBase(val builder: DftMatrixBuilder<ModularUInt>, val statistic: TaskTimingStatistic<TwoMatrix<ModularUInt>, AbstractMatrix<ModularUInt>> = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming())) {

    suspend fun test(range: UIntRange, repeat: UInt = 1u) {
        for (i in range) {
            val primeField = FieldModularUInt(primeOf(i).toUInt())
            val dft = DftMatrixBuilderModularUInt.compute(primeField.firstGenerator)
            util.stdlib.repeat(repeat) {
                val x = primeField.randomMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
        }
        statistic.printAverageAndStandardDeviation()
        println("range: $range")
    }
}